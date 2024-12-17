package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.Application;
import en.via.sep3_t3.repositoryContracts.IApplicationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Repository implementation for managing {@link Application} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `Application` table to perform CRUD operations.</p>
 */
@Qualifier("ApplicationBase")
@Repository
public class ApplicationRepository implements IApplicationRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code ApplicationRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public ApplicationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves all applications from the database.
   *
   * @return a list of all {@link Application} entities.
   */
  @Override
  public List<Application> findAll() {
    String sql = "SELECT * FROM Application";
    return jdbcTemplate.query(sql, new ApplicationRowMapper());
  }

  /**
   * Retrieves an application by its composite key of listing ID and sitter ID.
   *
   * @param listing_id the listing ID of the application.
   * @param sitter_id the sitter ID of the application.
   * @return the {@link Application} matching the provided composite key.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no application matches the key.
   */
  @Override
  public Application findById(int listing_id, int sitter_id) {
    String sql = "SELECT * FROM Application WHERE listing_id = ? AND sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new ApplicationRowMapper(), listing_id, sitter_id);
  }

  /**
   * Saves a new application to the database.
   *
   * @param application the {@link Application} entity to save.
   */
  @Override
  public void save(Application application) {
    String sql = "INSERT INTO Application (listing_id, sitter_id, message, status, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        application.getListing_id(),
        application.getSitter_id(),
        application.getMessage(),
        application.getStatus(),
        new Timestamp(ZonedDateTime.of(application.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  /**
   * Updates the status of an existing application in the database.
   *
   * @param application the {@link Application} entity to update.
   * @return the updated {@link Application} entity.
   */
  @Override
  public Application update(Application application) {
    String sql = "UPDATE Application SET status = ? WHERE listing_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql,
        application.getStatus(),
        application.getListing_id(),
        application.getSitter_id());
    return findById(application.getListing_id(), application.getSitter_id());
  }

  /**
   * Deletes an application by its composite key of listing ID and sitter ID.
   *
   * @param listing_id the listing ID of the application to delete.
   * @param sitter_id the sitter ID of the application to delete.
   */
  @Override
  public void deleteById(int listing_id, int sitter_id) {
    String sql = "DELETE FROM Application WHERE listing_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, listing_id, sitter_id);
  }

  /**
   * Private static class for mapping {@link Application} rows from the database.
   */
  private static class ApplicationRowMapper implements RowMapper<Application> {

    /**
     * Maps a single row of the `Application` table to an {@link Application} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link Application} object.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
      Application application = new Application();
      application.setListing_id(rs.getInt("listing_id"));
      application.setSitter_id(rs.getInt("sitter_id"));
      application.setMessage(rs.getString("message"));
      application.setStatus(rs.getString("status"));
      application.setDate(rs.getTimestamp("date").toLocalDateTime());
      return application;
    }
  }
}

