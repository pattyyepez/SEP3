package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseSitter;

import java.util.List;

/**
 * Repository interface for managing {@link HouseSitter} entities.
 * Provides methods to perform CRUD operations on house sitters, such as retrieving, saving, updating, and deleting.
 */
public interface IHouseSitterRepository {

  /**
   * Retrieves all {@link HouseSitter} entities.
   *
   * @return a list of all {@link HouseSitter} entities.
   */
  List<HouseSitter> findAll();

  /**
   * Retrieves all the skills available for house sitters.
   *
   * @return a list of skill names.
   */
  List<String> findAllSkills();

  /**
   * Retrieves a specific {@link HouseSitter} by the sitter's ID.
   *
   * @param id the ID of the house sitter.
   * @return the {@link HouseSitter} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house sitter is found with the given ID.
   */
  HouseSitter findById(int id);

  /**
   * Saves a new {@link HouseSitter} to the database.
   *
   * @param houseSitter the {@link HouseSitter} entity to save.
   * @return the ID of the newly saved house sitter.
   */
  int save(HouseSitter houseSitter);

  /**
   * Updates an existing {@link HouseSitter} in the database.
   *
   * @param houseSitter the {@link HouseSitter} entity with updated information.
   */
  void update(HouseSitter houseSitter);

  /**
   * Deletes a {@link HouseSitter} by the sitter's ID.
   *
   * @param id the ID of the house sitter to delete.
   */
  void deleteById(int id);
}

