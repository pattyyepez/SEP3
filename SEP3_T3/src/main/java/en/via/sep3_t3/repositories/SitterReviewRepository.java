package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.SitterReview;
import en.via.sep3_t3.repositoryContracts.ISitterReviewRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Repository implementation for managing {@link SitterReview} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `Sitter_review` table to perform CRUD operations on sitter reviews,
 * including saving, updating, deleting, and retrieving reviews for sitters.</p>
 */
@Qualifier("SitterReviewBase")
@Repository
public class SitterReviewRepository implements ISitterReviewRepository {

  /**
   * The {@link JdbcTemplate} instance used for executing SQL queries.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code SitterReviewRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} to use for database queries.
   */
  public SitterReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves a sitter review by the specified owner and sitter IDs.
   *
   * @param ownerId the ID of the owner who created the review.
   * @param sitterId the ID of the sitter being reviewed.
   * @return the {@link SitterReview} entity matching the owner and sitter IDs.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no review is found with the given IDs.
   */
  public SitterReview findById(int ownerId, int sitterId) {
    String sql = "SELECT * FROM Sitter_review WHERE owner_id = ? AND sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new SitterReviewRowMapper(), ownerId, sitterId);
  }

  /**
   * Retrieves all sitter reviews from the database.
   *
   * @return a list of all {@link SitterReview} entities.
   */
  public List<SitterReview> findAll() {
    String sql = "SELECT * FROM Sitter_review";
    return jdbcTemplate.query(sql, new SitterReviewRowMapper());
  }

  /**
   * Saves a new {@link SitterReview} to the database.
   *
   * @param sitterReview the {@link SitterReview} entity to save.
   */
  public void save(SitterReview sitterReview) {
    String sql = "INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, sitterReview.getOwner_id(), sitterReview.getSitter_id(),
        sitterReview.getRating(), sitterReview.getComment(),
        new Timestamp(ZonedDateTime.of(sitterReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  /**
   * Updates an existing {@link SitterReview} in the database.
   *
   * @param sitterReview the {@link SitterReview} entity with updated information.
   * @return the updated {@link SitterReview} entity.
   */
  public SitterReview update(SitterReview sitterReview) {
    String sql = "UPDATE Sitter_review SET rating = ?, comments = ?, date = ? WHERE owner_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, sitterReview.getRating(), sitterReview.getComment(),
        new Timestamp(ZonedDateTime.of(sitterReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()),
        sitterReview.getOwner_id(), sitterReview.getSitter_id());
    return findById(sitterReview.getOwner_id(), sitterReview.getSitter_id());
  }

  /**
   * Deletes a sitter review by the specified owner and sitter IDs.
   *
   * @param ownerId the ID of the owner who created the review.
   * @param sitterId the ID of the sitter being reviewed.
   */
  public void deleteById(int ownerId, int sitterId) {
    String sql = "DELETE FROM Sitter_review WHERE owner_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, ownerId, sitterId);
  }

  /**
   * Private static class for mapping rows of the `Sitter_review` table to {@link SitterReview} entities.
   */
  private static class SitterReviewRowMapper implements RowMapper<SitterReview> {

    /**
     * Maps a single row of the `Sitter_review` table to a {@link SitterReview} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link SitterReview} entity.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public SitterReview mapRow(ResultSet rs, int rowNum) throws SQLException {
      SitterReview sitterReview = new SitterReview();
      sitterReview.setOwner_id(rs.getInt("owner_id"));
      sitterReview.setSitter_id(rs.getInt("sitter_id"));
      sitterReview.setRating(rs.getInt("rating"));
      sitterReview.setComment(rs.getString("comments"));
      sitterReview.setDate(rs.getTimestamp("date").toLocalDateTime());
      return sitterReview;
    }
  }
}

