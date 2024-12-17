package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.SitterReview;

import java.util.List;

/**
 * Repository interface for managing {@link SitterReview} entities.
 * Provides methods to perform CRUD operations on sitter reviews, such as retrieving, saving, updating, and deleting.
 */
public interface ISitterReviewRepository {

  /**
   * Retrieves all {@link SitterReview} entities.
   *
   * @return a list of all {@link SitterReview} entities.
   */
  List<SitterReview> findAll();

  /**
   * Retrieves a specific {@link SitterReview} by its composite IDs: owner ID and sitter ID.
   *
   * @param ownerId the ID of the owner providing the review.
   * @param sitterId the ID of the sitter being reviewed.
   * @return the {@link SitterReview} entity matching the given owner and sitter IDs.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no review is found for the given owner and sitter IDs.
   */
  SitterReview findById(int ownerId, int sitterId);

  /**
   * Saves a new {@link SitterReview} to the database.
   *
   * @param sitterReview the {@link SitterReview} entity to save.
   */
  void save(SitterReview sitterReview);

  /**
   * Updates an existing {@link SitterReview} in the database.
   *
   * @param sitterReview the {@link SitterReview} entity with updated information.
   * @return the updated {@link SitterReview} entity.
   */
  SitterReview update(SitterReview sitterReview);

  /**
   * Deletes a {@link SitterReview} by the given composite IDs: owner ID and sitter ID.
   *
   * @param ownerId the ID of the owner who gave the review.
   * @param sitterId the ID of the sitter being reviewed.
   */
  void deleteById(int ownerId, int sitterId);
}

