package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.Application;
import en.via.sep3_t3.repositoryContracts.IApplicationRepository;
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
public class ApplicationValProxy implements IApplicationRepository
{

  private final IApplicationRepository repo;
  private final ArrayList<Field> fields;

  public ApplicationValProxy(@Qualifier("ApplicationBase") IApplicationRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(Application.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<Application> findAll()
  {
    return repo.findAll();
  }

  @Override public Application findById(int listing_id, int sitter_id) {
    try{
      return repo.findById(listing_id, sitter_id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "Application with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
    }
  }

  @Override public void save(Application application)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(application) != null &&
            ((String) field.get(application)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new application.");
        }

      }
      repo.save(application);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "HouseSitter with Id '"+application.getSitter_id()+"' has already applied for listing with Id '"+application.getListing_id()+"'.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  @Override public Application update(Application application)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(application) != null &&
            ((String) field.get(application)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating an application.");
        }

      }
      return repo.update(application);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "Application with the composite ID '" + application.getListing_id() + ", " + application.getSitter_id() + "' does not exist.");
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "HouseSitter with Id '"+application.getSitter_id()+"' has already applied for listing with Id '"+application.getListing_id()+"'.");
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

  @Override public void deleteById(int listing_id, int sitter_id)
  {
    try{
      repo.deleteById(listing_id, sitter_id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "Application with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
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
