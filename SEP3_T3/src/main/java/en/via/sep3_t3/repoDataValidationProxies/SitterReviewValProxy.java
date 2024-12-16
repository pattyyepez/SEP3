package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.SitterReview;
import en.via.sep3_t3.repositoryContracts.ISitterReviewRepository;
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
public class SitterReviewValProxy implements ISitterReviewRepository
{

  private final ISitterReviewRepository repo;
  private final ArrayList<Field> fields;

  public SitterReviewValProxy(@Qualifier("SitterReviewBase") ISitterReviewRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(SitterReview.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<SitterReview> findAll()
  {
    return repo.findAll();
  }

  @Override public SitterReview findById(int owner_id, int sitter_id) {
    try{
      return repo.findById(owner_id, sitter_id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + owner_id + ", " + sitter_id + "' does not exist.");
    }
  }

  @Override public void save(SitterReview review)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new sitter review.");
        }

      }
      repo.save(review);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "HouseOwner with Id '"+review.getOwner_id()+"' has already reviewed the HouseSitter with Id '"+review.getSitter_id()+"'.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  @Override public SitterReview update(SitterReview review)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a sitter review.");
        }

      }
      return repo.update(review);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + review.getOwner_id() +", " + review.getSitter_id() + "' does not exist.");
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "HouseOwner with Id '"+review.getOwner_id()+"' has already reviewed the HouseSitter with Id '"+review.getSitter_id()+"'.");
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

  @Override public void deleteById(int owner_id, int sitter_id)
  {
    try{
      repo.deleteById(owner_id, sitter_id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + owner_id + ", " + sitter_id + "' does not exist.");
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
