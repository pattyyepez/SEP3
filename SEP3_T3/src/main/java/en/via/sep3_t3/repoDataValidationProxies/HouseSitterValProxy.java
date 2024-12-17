package en.via.sep3_t3.repoDataValidationProxies;

import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.domain.User;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
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
 * Service implementation for validating and delegating operations on house sitters.
 * This class acts as a proxy to enforce validation rules and handle exceptions
 * before delegating operations to the underlying repository.
 *
 * <p>It ensures that house sitter fields meet required criteria during creation or updates
 * and provides consistent exception handling for operations.</p>
 */
@Service
@Primary
public class HouseSitterValProxy implements IHouseSitterRepository {

  /**
   * The underlying house sitter repository to which operations are delegated.
   */
  private final IHouseSitterRepository repo;

  /**
   * A list of fields in the {@link User} and {@link HouseSitter} classes for runtime validation.
   */
  private final ArrayList<Field> fields;

  /**
   * Constructs a new {@code HouseSitterValProxy} with the specified repository.
   *
   * @param repo the underlying house sitter repository.
   */
  public HouseSitterValProxy(@Qualifier("HouseSitterBase") IHouseSitterRepository repo) {
    this.repo = repo;
    this.fields = new ArrayList<>();
    fields.addAll(List.of(User.class.getDeclaredFields()));
    fields.addAll(List.of(HouseSitter.class.getDeclaredFields()));
    fields.forEach(field -> field.setAccessible(true));
  }

  /**
   * Retrieves all house sitters.
   *
   * @return a list of all {@link HouseSitter} objects.
   */
  @Override
  public List<HouseSitter> findAll() {
    return repo.findAll();
  }

  /**
   * Retrieves all available skills for house sitters.
   *
   * @return a list of skills as strings.
   */
  @Override
  public List<String> findAllSkills() {
    return repo.findAllSkills();
  }

  /**
   * Retrieves a house sitter by their ID.
   *
   * @param id the ID of the house sitter.
   * @return the {@link HouseSitter} object matching the given ID.
   * @throws StatusRuntimeException if the house sitter does not exist.
   */
  @Override
  public HouseSitter findById(int id) {
    try {
      return repo.findById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseSitter with the ID '" + id + "' does not exist.");
    }
  }

  /**
   * Saves a new house sitter after validating its fields.
   *
   * @param houseSitter the {@link HouseSitter} object to save.
   * @return the ID of the newly saved house sitter.
   * @throws StatusRuntimeException if required fields are blank, data integrity is violated, or duplicates exist.
   */
  @Override
  public int save(HouseSitter houseSitter) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) &&
            ((String) field.get(houseSitter)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when creating a new account.");
        }
      }

      if (houseSitter.getSkills().size() == 1 && houseSitter.getSkills().contains("")) {
        throw getException("", "You must select at least 1 skill.");
      }
      if (houseSitter.getPictures().size() < 3) {
        throw getException("", "You must upload at least 3 pictures when creating an account.");
      }

      return repo.save(houseSitter);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "The email '" + houseSitter.getEmail() + "' is already in use. Please use a different email.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IndexOutOfBoundsException e) {
      deleteById(houseSitter.getUserId());
      throw getException(e.getMessage(), "One of the given skills is not applicable/present in the database.");
    }
  }

  /**
   * Updates an existing house sitter after validating its fields.
   *
   * @param houseSitter the {@link HouseSitter} object to update.
   * @throws StatusRuntimeException if required fields are blank, the house sitter does not exist, or data integrity is violated.
   */
  @Override
  public void update(HouseSitter houseSitter) {
    try {
      for (Field field : fields) {
        if (field.getType().isAssignableFrom(String.class) &&
            (!field.getName().equals("email") && !field.getName().equals("password")) &&
            ((String) field.get(houseSitter)).isBlank()) {
          throw getException("", "Field '" + field.getName() + "' cannot be left blank when updating your account.");
        }
      }

      repo.update(houseSitter);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseSitter with the ID '" + houseSitter.getUserId() + "' does not exist.");
    } catch (DuplicateKeyException e) {
      throw getException(e.getMessage(), "The email '" + houseSitter.getEmail() + "' is already in use.");
    } catch (DataIntegrityViolationException e) {
      throw getException(e.getMessage(), "One of the entered values contains too many characters.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a house sitter by their ID.
   *
   * @param id the ID of the house sitter to delete.
   * @throws StatusRuntimeException if the house sitter does not exist.
   */
  @Override
  public void deleteById(int id) {
    try {
      repo.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw getException(e.getMessage(), "HouseSitter with the ID '" + id + "' does not exist.");
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

