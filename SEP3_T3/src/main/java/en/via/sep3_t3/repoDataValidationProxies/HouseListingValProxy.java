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

/**
 * Service implementation for a repository ensuring the persistence and data integrity of HouseListings.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It validates fields when saving or updating house listings and ensures that
 * data integrity rules are enforced.</p>
 */
@Service
@Primary
public class HouseListingValProxy implements IHouseListingRepository {

  /**
   * The underlying house listing repository to which operations are delegated.
   */
  private final IHouseListingRepository repo;

  /**
   * A list of fields in the {@link HouseListing} class for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code HouseListingValProxy} with the specified repository.
   *
   * @param repo the underlying house listing repository.
   */
  public HouseListingValProxy(@Qualifier("HouseListingBase") IHouseListingRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseListing.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all house listings.
   *
   * @return a list of all {@link HouseListing} objects.
   */
  @Override
  public List<HouseListing> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves a house listing by its ID.
   *
   * @param id the ID of the house listing.
   * @return the {@link HouseListing} object matching the given ID.
   * @throws StatusRuntimeException if the house listing does not exist.
   */
  @Override
  public HouseListing findById(int id) {
    try {
      return repo.findById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseListing with the ID '" + id + "' does not exist.");
    }
  }

  /**
   * Saves a new house listing after validating its fields.
   *
   * @param houseListing the {@link HouseListing} object to save.
   * @return the ID of the newly saved house listing.
   * @throws StatusRuntimeException if any string field is blank or data integrity is violated.
   */
  @Override
  public int save(HouseListing houseListing) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(houseListing) != null &&
            ((String) field.get(houseListing)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house listing.");
        }
      }
      return repo.save(houseListing);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  /**
   * Updates an existing house listing after validating its fields.
   *
   * @param houseListing the {@link HouseListing} object to update.
   * @return the updated {@link HouseListing} object.
   * @throws StatusRuntimeException if any string field is blank, the house listing does not exist, or data integrity is violated.
   */
  @Override
  public HouseListing update(HouseListing houseListing) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(houseListing) != null &&
            ((String) field.get(houseListing)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house listing.");
        }
      }
      return repo.update(houseListing);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseListing with the ID '" + houseListing.getId() + "' does not exist.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a house listing by its ID.
   *
   * @param id the ID of the house listing to delete.
   * @throws StatusRuntimeException if the house listing does not exist.
   */
  @Override
  public void deleteById(int id) {
    try {
      repo.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseListing with the Id '" + id + "' does not exist.");
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
