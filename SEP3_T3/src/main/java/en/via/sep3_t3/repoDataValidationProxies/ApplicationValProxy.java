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

/**
 * Service implementation for a repository ensuring the persistence and data integrity of Applications.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It validates fields when saving or updating applications and ensures that
 * composite IDs and data integrity rules are enforced.</p>
 */
@Service
@Primary
public class ApplicationValProxy implements IApplicationRepository {

  /**
   * The underlying application repository to which operations are delegated.
   */
  private final IApplicationRepository repo;

  /**
   * A list of fields in the {@link Application} class for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code ApplicationValProxy} with the specified repository.
   *
   * @param repo the underlying application repository.
   */
  public ApplicationValProxy(@Qualifier("ApplicationBase") IApplicationRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(Application.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all applications.
   *
   * @return a list of all {@link Application} objects.
   */
  @Override
  public List<Application> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves an application by its composite ID consisting of listing ID and sitter ID.
   *
   * @param listing_id the listing ID of the application.
   * @param sitter_id  the sitter ID of the application.
   * @return the {@link Application} object matching the given IDs.
   * @throws StatusRuntimeException if the application does not exist.
   */
  @Override
  public Application findById(int listing_id, int sitter_id) {
    try {
      return repo.findById(listing_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "Application with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
    }
  }

  /**
   * Saves a new application after validating its fields.
   *
   * @param application the {@link Application} object to save.
   * @throws StatusRuntimeException if any string field is blank, the composite key exists, or data integrity is violated.
   */
  @Override
  public void save(Application application) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(application) != null &&
            ((String) field.get(application)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new application.");
        }
      }
      repo.save(application);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseSitter with Id '" + application.getSitter_id() + "' has already applied for listing with Id '" + application.getListing_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  /**
   * Updates an existing application after validating its fields.
   *
   * @param application the {@link Application} object to update.
   * @return the updated {@link Application} object.
   * @throws StatusRuntimeException if any string field is blank, the application does not exist, or data integrity is violated.
   */
  @Override
  public Application update(Application application) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(application) != null &&
            ((String) field.get(application)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating an application.");
        }
      }
      return repo.update(application);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "Application with the composite ID '" + application.getListing_id() + ", " + application.getSitter_id() + "' does not exist.");
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "HouseSitter with Id '" + application.getSitter_id() + "' has already applied for listing with Id '" + application.getListing_id() + "'.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes an application by its composite ID consisting of listing ID and sitter ID.
   *
   * @param listing_id the listing ID of the application to delete.
   * @param sitter_id  the sitter ID of the application to delete.
   * @throws StatusRuntimeException if the application does not exist.
   */
  @Override
  public void deleteById(int listing_id, int sitter_id) {
    try {
      repo.deleteById(listing_id, sitter_id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "Application with the composite ID '" + listing_id + ", " + sitter_id + "' does not exist.");
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

