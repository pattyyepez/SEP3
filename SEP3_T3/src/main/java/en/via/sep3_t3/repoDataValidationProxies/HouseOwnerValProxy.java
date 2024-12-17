package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.domain.User;
import en.via.sep3_t3.repositoryContracts.IHouseOwnerRepository;
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
 * Service implementation for validating and delegating operations on house owners.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It validates fields when saving or updating house owner accounts and ensures
 * that data integrity rules are enforced.</p>
 */
@Service
@Primary
public class HouseOwnerValProxy implements IHouseOwnerRepository {

  /**
   * The underlying house owner repository to which operations are delegated.
   */
  private final IHouseOwnerRepository repo;

  /**
   * A list of fields in the {@link User} and {@link HouseOwner} classes for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code HouseOwnerValProxy} with the specified repository.
   *
   * @param repo the underlying house owner repository.
   */
  public HouseOwnerValProxy(@Qualifier("HouseOwnerBase") IHouseOwnerRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(User.class.getDeclaredFields()));
    fields.addAll(List.of(HouseOwner.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all house owners.
   *
   * @return a list of all {@link HouseOwner} objects.
   */
  @Override
  public List<HouseOwner> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves a house owner by their ID.
   *
   * @param id the ID of the house owner.
   * @return the {@link HouseOwner} object matching the given ID.
   * @throws StatusRuntimeException if the house owner does not exist.
   */
  @Override
  public HouseOwner findById(int id) {
    try {
      return repo.findById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseOwner with the ID '" + id + "' does not exist.");
    }
  }

  /**
   * Saves a new house owner after validating its fields.
   *
   * @param houseOwner the {@link HouseOwner} object to save.
   * @return the ID of the newly saved house owner.
   * @throws StatusRuntimeException if any string field is blank, a profile picture is missing, or data integrity is violated.
   */
  @Override
  public int save(HouseOwner houseOwner) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseOwner)).isBlank()) {
          if (field.getName().equals("profilePicture"))
            throw getException("", "Please upload a profile picture before creating an account.");

          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new account.");
        }
      }
      return repo.save(houseOwner);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "The email '" + houseOwner.getEmail() + "' is already in use. Please use a different email.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    }
  }

  /**
   * Updates an existing house owner after validating its fields.
   *
   * @param houseOwner the {@link HouseOwner} object to update.
   * @throws StatusRuntimeException if any string field is blank, the house owner does not exist, or data integrity is violated.
   */
  @Override
  public void update(HouseOwner houseOwner) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) &&
            (!field.getName().equals("email") && !field.getName().equals("password")) &&
            ((String) field.get(houseOwner)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new account.");
        }
      }

      repo.update(houseOwner);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseOwner with the ID '" + houseOwner.getUserId() + "' does not exist.");
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "The email '" + houseOwner.getEmail() + "' is already in use.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a house owner by their ID.
   *
   * @param id the ID of the house owner to delete.
   * @throws StatusRuntimeException if the house owner does not exist.
   */
  @Override
  public void deleteById(int id) {
    try {
      repo.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseOwner with the ID '" + id + "' does not exist.");
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
