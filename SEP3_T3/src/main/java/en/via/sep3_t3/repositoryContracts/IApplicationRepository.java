package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.Application;

import java.util.List;

/**
 * Repository interface for managing {@link Application} entities.
 * Provides methods to perform CRUD operations on {@link Application} objects.
 */
public interface IApplicationRepository {

  /**
   * Retrieves all {@link Application} entities.
   *
   * @return a list of all {@link Application} entities.
   */
  List<Application> findAll();

  /**
   * Retrieves a specific {@link Application} by its composite IDs: listing ID and sitter ID.
   *
   * @param listing_id the ID of the listing for which the application was made.
   * @param sitter_id the ID of the sitter who applied.
   * @return the {@link Application} entity matching the given listing and sitter IDs.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no application is found with the given IDs.
   */
  Application findById(int listing_id, int sitter_id);

  /**
   * Saves a new {@link Application} to the database.
   *
   * @param application the {@link Application} entity to save.
   */
  void save(Application application);

  /**
   * Updates an existing {@link Application} in the database.
   *
   * @param application the {@link Application} entity with updated information.
   * @return the updated {@link Application} entity.
   */
  Application update(Application application);

  /**
   * Deletes an {@link Application} by its composite IDs: listing ID and sitter ID.
   *
   * @param listing_id the ID of the listing for which the application was made.
   * @param sitter_id the ID of the sitter who applied.
   */
  void deleteById(int listing_id, int sitter_id);
}

