package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Repository implementation for managing {@link HouseReview} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `House_review` table to perform CRUD operations.</p>
 */
@Qualifier("HouseReviewBase")
@Repository
public class HouseReviewRepository implements IHouseReviewRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code HouseReviewRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public HouseReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves a {@link HouseReview} by its profile ID and sitter ID.
   *
   * @param profileId the profile ID of the house owner.
   * @param sitterId the sitter ID of the house sitter.
   * @return the {@link HouseReview} matching the given profile ID and sitter ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no review is found with the given IDs.
   */
  public HouseReview findById(int profileId, int sitterId) {
    String sql = "SELECT * FROM House_review WHERE profile_id = ? AND sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseReviewRowMapper(), profileId, sitterId);
  }

  /**
   * Retrieves all {@link HouseReview} entities from the database.
   *
   * @return a list of all {@link HouseReview} entities.
   */
  public List<HouseReview> findAll() {
    String sql = "SELECT * FROM House_review";
    return jdbcTemplate.query(sql, new HouseReviewRowMapper());
  }

  /**
   * Saves a new {@link HouseReview} to the database.
   *
   * @param houseReview the {@link HouseReview} entity to save.
   */
  public void save(HouseReview houseReview) {
    String sql = "INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, houseReview.getProfile_id(), houseReview.getSitter_id(),
        houseReview.getRating(), houseReview.getComment(),
        new Timestamp(ZonedDateTime.of(houseReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  /**
   * Updates an existing {@link HouseReview} in the database.
   *
   * @param houseReview the {@link HouseReview} entity to update.
   * @return the updated {@link HouseReview} entity.
   */
  public HouseReview update(HouseReview houseReview) {
    String sql = "UPDATE House_review SET rating = ?, comments = ?, date = ? WHERE profile_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, houseReview.getRating(), houseReview.getComment(),
        new Timestamp(ZonedDateTime.of(houseReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()),
        houseReview.getProfile_id(), houseReview.getSitter_id());
    return findById(houseReview.getProfile_id(), houseReview.getSitter_id());
  }

  /**
   * Deletes a {@link HouseReview} by its profile ID and sitter ID.
   *
   * @param profileId the profile ID of the house owner.
   * @param sitterId the sitter ID of the house sitter.
   */
  public void deleteById(int profileId, int sitterId) {
    String sql = "DELETE FROM House_review WHERE profile_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, profileId, sitterId);
  }

  /**
   * Private static class for mapping {@link HouseReview} rows from the database.
   */
  private static class HouseReviewRowMapper implements RowMapper<HouseReview> {

    /**
     * Maps a single row of the `House_review` table to a {@link HouseReview} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link HouseReview} object.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public HouseReview mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseReview houseReview = new HouseReview();
      houseReview.setProfile_id(rs.getInt("profile_id"));
      houseReview.setSitter_id(rs.getInt("sitter_id"));
      houseReview.setRating(rs.getInt("rating"));
      houseReview.setComment(rs.getString("comments"));
      houseReview.setDate(rs.getTimestamp("date").toLocalDateTime());
      return houseReview;
    }
  }
}

