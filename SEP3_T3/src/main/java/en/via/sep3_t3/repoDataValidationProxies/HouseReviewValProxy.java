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

/**
 * Service implementation for validating and delegating operations on house reviews.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It ensures that house review fields meet required criteria during creation or updates
 * and provides consistent exception handling for operations.</p>
 */
@Service
@Primary
public class HouseReviewValProxy implements IHouseReviewRepository {

  /**
   * The underlying house review repository to which operations are delegated.
   */
  private final IHouseReviewRepository repo;

  /**
   * A list of fields in the {@link HouseReview} class for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code HouseReviewValProxy} with the specified repository.
   *
   * @param repo the underlying house review repository.
   */
  public HouseReviewValProxy(@Qualifier("HouseReviewBase") IHouseReviewRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseReview.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all house reviews.
   *
   * @return a list of all {@link HouseReview} objects.
   */
  @Override
  public List<HouseReview> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves a house review by its composite ID of profile ID and sitter ID.
   *
   * @param profile_id the ID of the house profile being reviewed.
   * @param sitter_id  the ID of the house sitter who made the review.
   * @return the {@link HouseReview} object matching the given composite ID.
   * @throws StatusRuntimeException if the house review does not exist.
   */
  @Override
  public HouseReview findById(int profile_id, int sitter_id) {
    try {
      return repo.findById(profile_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + profile_id + ", " + sitter_id + "' does not exist.");
    }
  }

  /**
   * Saves a new house review after validating its fields.
   *
   * @param review the {@link HouseReview} object to save.
   * @throws StatusRuntimeException if required fields are blank or data integrity is violated.
   */
  @Override
  public void save(HouseReview review) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house review.");
        }
      }
      repo.save(review);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseSitter with Id '" + review.getSitter_id() + "' has already reviewed the HouseProfile with Id '" + review.getProfile_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  /**
   * Updates an existing house review after validating its fields.
   *
   * @param review the {@link HouseReview} object to update.
   * @return the updated {@link HouseReview} object.
   * @throws StatusRuntimeException if required fields are blank, the house review does not exist, or data integrity is violated.
   */
  @Override
  public HouseReview update(HouseReview review) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house review.");
        }
      }
      return repo.update(review);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + review.getProfile_id() + ", " + review.getSitter_id() + "' does not exist.");
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseSitter with Id '" + review.getSitter_id() + "' has already reviewed the HouseProfile with Id '" + review.getProfile_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a house review by its composite ID of listing ID and sitter ID.
   *
   * @param listing_id the ID of the house listing being reviewed.
   * @param sitter_id  the ID of the house sitter who made the review.
   * @throws StatusRuntimeException if the house review does not exist.
   */
  @Override
  public void deleteById(int listing_id, int sitter_id) {
    try {
      repo.deleteById(listing_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseReview with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
    }
  }

  /**
   * Creates a gRPC {@link StatusRuntimeException} for consistent error handling.
   *
   * @param messageSpecific the specific error message for logging.
   * @param messageSimple   the simplified error message for the user.
   * @return a {@link StatusRuntimeException} with metadata.
   */
  private static StatusRuntimeException getException(String messageSpecific, String messageSimple) {
    Metadata metadata = new Metadata();
    Metadata.Key<String> errorKey = Metadata.Key.of("error-details", Metadata.ASCII_STRING_MARSHALLER);
    metadata.put(errorKey, messageSpecific);

    return Status.INTERNAL
        .withDescription(messageSimple)
        .asRuntimeException(metadata);
  }
}

