package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.Report;
import en.via.sep3_t3.repositoryContracts.IReportRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Repository implementation for managing {@link Report} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `Report` table to perform CRUD operations on reports, including saving,
 * updating, deleting, and retrieving report data from the database.</p>
 */
@Qualifier("ReportBase")
@Repository
public class ReportRepository implements IReportRepository {

  /**
   * The {@link JdbcTemplate} instance used for executing SQL queries.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code ReportRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} to use for database queries.
   */
  public ReportRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves a report by its ID from the database.
   *
   * @param id the ID of the report to retrieve.
   * @return the {@link Report} entity with the specified ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no report is found with the given ID.
   */
  public Report findById(int id) {
    String sql = "SELECT * FROM Report WHERE report_id = ?";
    return jdbcTemplate.queryForObject(sql, new ReportRowMapper(), id);
  }

  /**
   * Retrieves all reports from the database.
   *
   * @return a list of all {@link Report} entities.
   */
  public List<Report> findAll() {
    String sql = "SELECT * FROM Report";
    return jdbcTemplate.query(sql, new ReportRowMapper());
  }

  /**
   * Saves a new {@link Report} to the database.
   *
   * @param report the {@link Report} entity to save.
   * @return the ID of the newly created report.
   */
  public int save(Report report) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Report (reporting_id, reported_id, admin_id, comments, date)"
              + " VALUES (?, ?, NULL, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, report.getReporting_id());
      ps.setInt(2, report.getReported_id());
      ps.setString(3, report.getComment());
      ps.setTimestamp(4, new Timestamp(
          ZonedDateTime.of(report.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("report_id");
  }

  /**
   * Updates the status of an existing report in the database.
   *
   * @param report the {@link Report} entity with updated status.
   */
  public void update(Report report) {
    String sql = "UPDATE Report SET status = ? WHERE report_id = ?";
    jdbcTemplate.update(sql, report.getStatus(), report.getId());
  }

  /**
   * Deletes a report by its ID from the database.
   *
   * @param id the ID of the report to delete.
   */
  public void deleteById(int id) {
    String sql = "DELETE FROM Report WHERE report_id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Private static class for mapping rows of the `Report` table to {@link Report} entities.
   */
  private static class ReportRowMapper implements RowMapper<Report> {

    /**
     * Maps a single row of the `Report` table to a {@link Report} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link Report} entity.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
      Report report = new Report();
      report.setId(rs.getInt("report_id"));
      report.setReporting_id(rs.getInt("reporting_id"));
      report.setReported_id(rs.getInt("reported_id"));
      report.setAdmin_id(rs.getInt("admin_id"));
      report.setComment(rs.getString("comments"));
      report.setStatus(rs.getString("status"));
      report.setDate(rs.getTimestamp("date").toLocalDateTime());
      return report;
    }
  }
}

