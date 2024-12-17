package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseListing;

import java.util.List;

/**
 * Repository interface for managing {@link HouseListing} entities.
 * Provides methods to perform CRUD operations on {@link HouseListing} objects.
 */
public interface IHouseListingRepository {

  /**
   * Retrieves all {@link HouseListing} entities.
   *
   * @return a list of all {@link HouseListing} entities.
   */
  List<HouseListing> findAll();

  /**
   * Retrieves a specific {@link HouseListing} by its ID.
   *
   * @param id the ID of the house listing.
   * @return the {@link HouseListing} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house listing is found with the given ID.
   */
  HouseListing findById(int id);

  /**
   * Saves a new {@link HouseListing} to the database.
   *
   * @param houseListing the {@link HouseListing} entity to save.
   * @return the ID of the newly saved house listing.
   */
  int save(HouseListing houseListing);

  /**
   * Updates an existing {@link HouseListing} in the database.
   *
   * @param houseListing the {@link HouseListing} entity with updated information.
   * @return the updated {@link HouseListing} entity.
   */
  HouseListing update(HouseListing houseListing);

  /**
   * Deletes a {@link HouseListing} by its ID.
   *
   * @param id the ID of the house listing to delete.
   */
  void deleteById(int id);
}

