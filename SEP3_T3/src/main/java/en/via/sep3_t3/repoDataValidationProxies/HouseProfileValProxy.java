package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseProfile;
import en.via.sep3_t3.repositoryContracts.IHouseProfileRepository;
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
public class HouseProfileValProxy implements IHouseProfileRepository
{

  private final IHouseProfileRepository repo;
  private final ArrayList<Field> fields;

  public HouseProfileValProxy(@Qualifier("HouseProfileBase") IHouseProfileRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseProfile.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<HouseProfile> findAll()
  {
    return repo.findAll();
  }

  @Override public List<String> findAllRules()
  {
    return repo.findAllRules();
  }

  @Override public List<String> findAllChores()
  {
    return repo.findAllChores();
  }

  @Override public List<String> findAllAmenities()
  {
    return repo.findAllAmenities();
  }

  @Override public HouseProfile findById(int id) {
    try{
      return repo.findById(id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseProfile with the ID '" + id + "' does not exist.");
    }
  }

  @Override public int save(HouseProfile houseProfile)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(houseProfile) != null &&
            ((String) field.get(houseProfile)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house profile.");
        }
      }

      if(houseProfile.getRules().size() < 3)
        throw getException("", "You pick at least 1 rule when creating a house profile.");

      if(houseProfile.getChores().size() < 3)
        throw getException("", "You pick at least 1 chore when creating a house profile.");

      if(houseProfile.getAmenities().size() < 3)
        throw getException("", "You pick at least 1 amenity when creating a house profile.");

      if(houseProfile.getPictures().size() < 3)
        throw getException("", "You must upload at least 3 pictures when creating a house profile.");

      return repo.save(houseProfile);
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

  @Override public HouseProfile update(HouseProfile houseProfile)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(houseProfile) != null &&
            ((String) field.get(houseProfile)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house profile.");
        }

      }
      return repo.update(houseProfile);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseProfile with the ID '" + houseProfile.getId() + "' does not exist.");
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
      throw getException(e.getMessage(), "HouseProfile with the Id '"+id+"' does not exist.");
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
