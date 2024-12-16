package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositoryContracts.IHouseListingRepository;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class HouseListingValProxy implements IHouseListingRepository
{

  private final IHouseListingRepository repo;
  private final ArrayList<Field> fields;

  public HouseListingValProxy(@Qualifier("HouseListingBase") IHouseListingRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseListing.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<HouseListing> findAll()
  {
    return repo.findAll();
  }

  @Override public HouseListing findById(int id) {
    try{
      return repo.findById(id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseListing with the ID '" + id + "' does not exist.");
    }
  }

  @Override public int save(HouseListing houseListing)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(houseListing) != null &&
            ((String) field.get(houseListing)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house listing.");
        }

      }
      return repo.save(houseListing);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  @Override public HouseListing update(HouseListing houseListing)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(houseListing) != null &&
            ((String) field.get(houseListing)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house listing.");
        }

      }
      return repo.update(houseListing);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseListing with the ID '" + houseListing.getId() + "' does not exist.");
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
      throw getException(e.getMessage(), "HouseListing with the Id '"+id+"' does not exist.");
    }
  }

  private static StatusRuntimeException getException(String messageSpecific, String messageSimple){
    Metadata metadata = new Metadata();
    Metadata.Key<String> errorKey = Metadata.Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER);
    metadata.put(errorKey, messageSpecific);

    return Status.INTERNAL
        .withDescription(messageSimple)
        .asRuntimeException(metadata);

  }
}
