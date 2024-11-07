package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.domain.User;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class HouseSitterValProxy implements IHouseSitterRepository
{
  private final IHouseSitterRepository repo;
  private final ArrayList<Field> fields;

  public HouseSitterValProxy(@Qualifier("HouseSitterBase") IHouseSitterRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(User.class.getDeclaredFields()));
    fields.addAll(List.of(HouseSitter.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<HouseSitter> findAll()
  {
    return repo.findAll();
  }

  @Override public HouseSitter findById(int id)
  {
    try{
      return repo.findById(id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseSitter with the ID '" + id + "' does not exist.");
    }
  }

  @Override public int save(HouseSitter houseSitter)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseSitter)).isBlank()
        ){
          throw getException("", "Value '" + field.getName() + "' cannot be left blank.");
        }
        if(houseSitter.getSkills().size() == 1 && houseSitter.getSkills().contains(""))
          throw getException("", "You must select at least 1 skill.");
        if(houseSitter.getPictures().size() < 3)
          throw getException("", "You must upload at least 3 pictures");

      }
      return repo.save(houseSitter);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "The email '" + houseSitter.getEmail() + "' is already in use.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
    catch (IndexOutOfBoundsException e)
    {
      deleteById(houseSitter.getUserId());
      throw getException(e.getMessage(), "One of the given skills is not applicable/present in the database.");
    }
  }

  @Override public void update(HouseSitter houseSitter)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseSitter)).isBlank()
        ){
          throw getException("", "Value '" + field.getName() + "' cannot be left blank when creating a new HouseSitter.");
        }

      }

      repo.update(houseSitter);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseSitter with the ID '" + houseSitter.getUserId() + "' does not exist.");
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "The email '" + houseSitter.getEmail() + "' is already in use.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void deleteById(int id)
  {
    try{
      repo.deleteById(id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseSitter with the ID '" + id + "' does not exist.");
    }
  }

  private static StatusRuntimeException getException(String messageSpecific, String messageSimple){
    Metadata metadata = new Metadata();
    Metadata.Key<String> errorKey = Metadata.Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER);
    metadata.put(errorKey, messageSpecific);

    return Status.INTERNAL
        .withDescription("An error occurred when using HouseSitter Repository: " + messageSimple)
        .asRuntimeException(metadata);

  }
}
