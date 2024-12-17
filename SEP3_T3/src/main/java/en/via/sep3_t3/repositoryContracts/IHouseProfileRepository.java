package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseProfile;

import java.util.List;

/**
 * Repository interface for managing {@link HouseProfile} entities.
 * Provides methods to perform CRUD operations and retrieve related data such as rules, chores, and amenities.
 */
public interface IHouseProfileRepository {

  /**
   * Retrieves all {@link HouseProfile} entities.
   *
   * @return a list of all {@link HouseProfile} entities.
   */
  List<HouseProfile> findAll();

  /**
   * Retrieves all house rules from the repository.
   *
   * @return a list of all house rules.
   */
  List<String> findAllRules();

  /**
   * Retrieves all house chores from the repository.
   *
   * @return a list of all house chores.
   */
  List<String> findAllChores();

  /**
   * Retrieves all amenities associated with house profiles.
   *
   * @return a list of all house amenities.
   */
  List<String> findAllAmenities();

  /**
   * Retrieves a specific {@link HouseProfile} by its ID.
   *
   * @param id the ID of the house profile.
   * @return the {@link HouseProfile} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house profile is found with the given ID.
   */
  HouseProfile findById(int id);

  /**
   * Saves a new {@link HouseProfile} to the database.
   *
   * @param houseProfile the {@link HouseProfile} entity to save.
   * @return the ID of the newly saved house profile.
   */
  int save(HouseProfile houseProfile);

  /**
   * Updates an existing {@link HouseProfile} in the database.
   *
   * @param houseProfile the {@link HouseProfile} entity with updated information.
   * @return the updated {@link HouseProfile} entity.
   */
  HouseProfile update(HouseProfile houseProfile);

  /**
   * Deletes a {@link HouseProfile} by its ID.
   *
   * @param id the ID of the house profile to delete.
   */
  void deleteById(int id);
}

