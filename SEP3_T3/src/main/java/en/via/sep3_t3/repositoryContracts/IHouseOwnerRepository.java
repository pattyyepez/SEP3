package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseOwner;

import java.util.List;

/**
 * Repository interface for managing {@link HouseOwner} entities.
 * Provides methods to perform CRUD operations on {@link HouseOwner} objects.
 */
public interface IHouseOwnerRepository {

  /**
   * Retrieves all {@link HouseOwner} entities.
   *
   * @return a list of all {@link HouseOwner} entities.
   */
  List<HouseOwner> findAll();

  /**
   * Retrieves a specific {@link HouseOwner} by its ID.
   *
   * @param id the ID of the house owner.
   * @return the {@link HouseOwner} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house owner is found with the given ID.
   */
  HouseOwner findById(int id);

  /**
   * Saves a new {@link HouseOwner} to the database.
   *
   * @param houseOwner the {@link HouseOwner} entity to save.
   * @return the ID of the newly saved house owner.
   */
  int save(HouseOwner houseOwner);

  /**
   * Updates an existing {@link HouseOwner} in the database.
   *
   * @param houseOwner the {@link HouseOwner} entity with updated information.
   */
  void update(HouseOwner houseOwner);

  /**
   * Deletes a {@link HouseOwner} by its ID.
   *
   * @param id the ID of the house owner to delete.
   */
  void deleteById(int id);
}

