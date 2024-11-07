package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.domain.User;
import en.via.sep3_t3.repositoryContracts.IHouseOwnerRepository;
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
public class HouseOwnerValProxy implements IHouseOwnerRepository
{
  private final IHouseOwnerRepository repo;
  private final ArrayList<Field> fields;

  public HouseOwnerValProxy(@Qualifier("HouseOwnerBase") IHouseOwnerRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(User.class.getDeclaredFields()));
    fields.addAll(List.of(HouseOwner.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<HouseOwner> findAll()
  {
    return repo.findAll();
  }

  @Override public HouseOwner findById(int id)
  {
    try{
      return repo.findById(id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseOwner with the ID '" + id + "' does not exist.");
    }
  }

  @Override public int save(HouseOwner houseOwner)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseOwner)).isBlank()
        ){
          throw getException("", "Value '" + field.getName() + "' cannot be left blank.");
        }

      }
      return repo.save(houseOwner);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "The email '" + houseOwner.getEmail() + "' is already in use.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  @Override public void update(HouseOwner houseOwner)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseOwner)).isBlank()
        ){
          throw getException("", "Value '" + field.getName() + "' cannot be left blank when creating a new HouseOwner.");
        }

      }

      repo.update(houseOwner);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseOwner with the ID '" + houseOwner.getUserId() + "' does not exist.");
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "The email '" + houseOwner.getEmail() + "' is already in use.");
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
      throw getException(e.getMessage(), "HouseOwner with the ID '" + id + "' does not exist.");
    }
  }

  private static StatusRuntimeException getException(String messageSpecific, String messageSimple){
    Metadata metadata = new Metadata();
    Metadata.Key<String> errorKey = Metadata.Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER);
    metadata.put(errorKey, messageSpecific);

    return Status.INTERNAL
        .withDescription("An error occurred when using HouseOwner Repository: " + messageSimple)
        .asRuntimeException(metadata);

  }
}
