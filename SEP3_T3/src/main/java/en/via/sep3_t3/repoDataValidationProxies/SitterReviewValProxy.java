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

/**
 * Service implementation for validating and delegating operations on sitter reviews.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It ensures that sitter review fields meet required criteria during creation or updates
 * and provides consistent exception handling for operations.</p>
 */
@Service
@Primary
public class SitterReviewValProxy implements ISitterReviewRepository {

  /**
   * The underlying sitter review repository to which operations are delegated.
   */
  private final ISitterReviewRepository repo;

  /**
   * A list of fields in the {@link SitterReview} class for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code SitterReviewValProxy} with the specified repository.
   *
   * @param repo the underlying sitter review repository.
   */
  public SitterReviewValProxy(@Qualifier("SitterReviewBase") ISitterReviewRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(SitterReview.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all sitter reviews.
   *
   * @return a list of all {@link SitterReview} objects.
   */
  @Override
  public List<SitterReview> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves a sitter review by its composite ID (owner ID and sitter ID).
   *
   * @param owner_id the ID of the house owner.
   * @param sitter_id the ID of the house sitter.
   * @return the {@link SitterReview} matching the given composite ID.
   * @throws StatusRuntimeException if the sitter review does not exist.
   */
  @Override
  public SitterReview findById(int owner_id, int sitter_id) {
    try {
      return repo.findById(owner_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + owner_id + ", " + sitter_id + "' does not exist.");
    }
  }

  /**
   * Saves a new sitter review after validating its fields.
   *
   * @param review the {@link SitterReview} object to save.
   * @throws StatusRuntimeException if required fields are blank, data integrity is violated, or duplicates exist.
   */
  @Override
  public void save(SitterReview review) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new sitter review.");
        }
      }
      repo.save(review);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseOwner with Id '" + review.getOwner_id() + "' has already reviewed the HouseSitter with Id '" + review.getSitter_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  /**
   * Updates an existing sitter review after validating its fields.
   *
   * @param review the {@link SitterReview} object to update.
   * @return the updated {@link SitterReview} object.
   * @throws StatusRuntimeException if required fields are blank, the sitter review does not exist, or data integrity is violated.
   */
  @Override
  public SitterReview update(SitterReview review) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(review) != null &&
            ((String) field.get(review)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a sitter review.");
        }
      }
      return repo.update(review);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + review.getOwner_id() + ", " + review.getSitter_id() + "' does not exist.");
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseOwner with Id '" + review.getOwner_id() + "' has already reviewed the HouseSitter with Id '" + review.getSitter_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a sitter review by its composite ID (owner ID and sitter ID).
   *
   * @param owner_id the ID of the house owner.
   * @param sitter_id the ID of the house sitter.
   * @throws StatusRuntimeException if the sitter review does not exist.
   */
  @Override
  public void deleteById(int owner_id, int sitter_id) {
    try {
      repo.deleteById(owner_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "SitterReview with the composite ID '" + owner_id + ", " + sitter_id + "' does not exist.");
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

