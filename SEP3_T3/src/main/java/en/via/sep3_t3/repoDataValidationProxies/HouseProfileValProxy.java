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

/**
 * Service implementation for validating and delegating operations on house profiles.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It validates fields and ensures that house profile rules, chores, amenities, and pictures
 * meet required criteria during creation or updates.</p>
 */
@Service
@Primary
public class HouseProfileValProxy implements IHouseProfileRepository {

  /**
   * The underlying house profile repository to which operations are delegated.
   */
  private final IHouseProfileRepository repo;

  /**
   * A list of fields in the {@link HouseProfile} class for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code HouseProfileValProxy} with the specified repository.
   *
   * @param repo the underlying house profile repository.
   */
  public HouseProfileValProxy(@Qualifier("HouseProfileBase") IHouseProfileRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(HouseProfile.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all house profiles.
   *
   * @return a list of all {@link HouseProfile} objects.
   */
  @Override
  public List<HouseProfile> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves all house rules.
   *
   * @return a list of all house rules as strings.
   */
  @Override
  public List<String> findAllRules() {
    return repo.findAllRules();
  }

  /**
   * Retrieves all house chores.
   *
   * @return a list of all house chores as strings.
   */
  @Override
  public List<String> findAllChores() {
    return repo.findAllChores();
  }

  /**
   * Retrieves all house amenities.
   *
   * @return a list of all house amenities as strings.
   */
  @Override
  public List<String> findAllAmenities() {
    return repo.findAllAmenities();
  }

  /**
   * Retrieves a house profile by its ID.
   *
   * @param id the ID of the house profile.
   * @return the {@link HouseProfile} object matching the given ID.
   * @throws StatusRuntimeException if the house profile does not exist.
   */
  @Override
  public HouseProfile findById(int id) {
    try {
      return repo.findById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseProfile with the ID '" + id + "' does not exist.");
    }
  }

  /**
   * Saves a new house profile after validating its fields.
   *
   * @param houseProfile the {@link HouseProfile} object to save.
   * @return the ID of the newly saved house profile.
   * @throws StatusRuntimeException if required fields are blank, lists are empty, or data integrity is violated.
   */
  @Override
  public int save(HouseProfile houseProfile) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(houseProfile) != null &&
            ((String) field.get(houseProfile)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new house profile.");
        }
      }

      if (houseProfile.getRules().isEmpty())
        throw getException("", "You must pick at least 1 rule when creating a house profile.");

      if (houseProfile.getChores().isEmpty())
        throw getException("", "You must pick at least 1 chore when creating a house profile.");

      if (houseProfile.getAmenities().isEmpty())
        throw getException("", "You must pick at least 1 amenity when creating a house profile.");

      if (houseProfile.getPictures().size() < 3)
        throw getException("", "You must upload at least 3 pictures when creating a house profile.");

      return repo.save(houseProfile);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "A given Rule, Chore or Amenity is not present in the database.");
    }
  }

  /**
   * Updates an existing house profile after validating its fields.
   *
   * @param houseProfile the {@link HouseProfile} object to update.
   * @return the updated {@link HouseProfile} object.
   * @throws StatusRuntimeException if required fields are blank, the house profile does not exist, or data integrity is violated.
   */
  @Override
  public HouseProfile update(HouseProfile houseProfile) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) && field.get(houseProfile) != null &&
            ((String) field.get(houseProfile)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating a house profile.");
        }
      }
      return repo.update(houseProfile);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseProfile with the ID '" + houseProfile.getId() + "' does not exist.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a house profile by its ID.
   *
   * @param id the ID of the house profile to delete.
   * @throws StatusRuntimeException if the house profile does not exist.
   */
  @Override
  public void deleteById(int id) {
    try {
      repo.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseProfile with the Id '" + id + "' does not exist.");
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
