package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseReview;

import java.util.List;

/**
 * Repository interface for managing {@link HouseReview} entities.
 * Provides methods to perform CRUD operations on house reviews, such as retrieving, saving, updating, and deleting.
 */
public interface IHouseReviewRepository {

  /**
   * Retrieves all {@link HouseReview} entities.
   *
   * @return a list of all {@link HouseReview} entities.
   */
  List<HouseReview> findAll();

  /**
   * Retrieves a specific {@link HouseReview} by the composite IDs: profile ID and sitter ID.
   *
   * @param profileId the ID of the house profile.
   * @param sitterId the ID of the house sitter.
   * @return the {@link HouseReview} entity matching the given profile and sitter IDs.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house review is found with the given IDs.
   */
  HouseReview findById(int profileId, int sitterId);

  /**
   * Saves a new {@link HouseReview} to the database.
   *
   * @param houseReview the {@link HouseReview} entity to save.
   */
  void save(HouseReview houseReview);

  /**
   * Updates an existing {@link HouseReview} in the database.
   *
   * @param houseReview the {@link HouseReview} entity with updated information.
   * @return the updated {@link HouseReview} entity.
   */
  HouseReview update(HouseReview houseReview);

  /**
   * Deletes a {@link HouseReview} by its composite IDs: profile ID and sitter ID.
   *
   * @param profileId the ID of the house profile.
   * @param sitterId the ID of the house sitter.
   */
  void deleteById(int profileId, int sitterId);
}

