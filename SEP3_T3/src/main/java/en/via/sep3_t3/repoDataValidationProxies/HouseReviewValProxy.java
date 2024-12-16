package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
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
public class HouseReviewValProxy implements IHouseReviewRepository
{

  private final IHouseReviewRepository repo;
  private final ArrayList<Field> fields;

  public HouseReviewValProxy(@Qualifier("HouseReviewBase") IHouseReviewRepository repo){
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseReview.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  @Override public List<HouseReview> findAll()
  {
    return repo.findAll();
  }

  @Override public HouseReview findById(int profile_id, int sitter_id) {
    try{
      return repo.findById(profile_id, sitter_id);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + profile_id + ", " + sitter_id + "' does not exist.");
    }
  }

  @Override public void save(HouseReview review)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house review.");
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
      throw getException(e.getMessage(), "HouseSitter with Id '"+review.getSitter_id()+"' has already reviewed the HouseProfile with Id '"+review.getProfile_id()+"'.");
    }
    catch (DataIntegrityViolationException e)
    {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  @Override public HouseReview update(HouseReview review)
  {
    try{
      for (Field field : fields){
        if(field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()
        ){
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house review.");
        }

      }
      return repo.update(review);
    }
    catch (EmptyResultDataAccessException e){
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + review.getProfile_id() +", " + review.getSitter_id() + "' does not exist.");
    }
    catch (DuplicateKeyException e)
    {
      throw getException(e.getMessage(), "HouseSitter with Id '"+review.getSitter_id()+"' has already reviewed the HouseProfile with Id '"+review.getProfile_id()+"'.");
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
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
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
