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

@Qualifier("ReportBase")
@Repository
public class ReportRepository implements IReportRepository
{
  private final JdbcTemplate jdbcTemplate;

  public ReportRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Report findById(int id)
  {
    String sql = "SELECT * FROM Report WHERE report_id = ?";
    return jdbcTemplate.queryForObject(sql, new ReportRowMapper(), id);
  }

  public List<Report> findAll()
  {
    String sql = "SELECT * FROM Report";
    return jdbcTemplate.query(sql, new ReportRowMapper());
  }

  public int save(Report report)
  {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Report (reporting_id, reported_id, admin_id, comments, date)"
              + " VALUES (?, ?, NULL, ?, ?)"
          // CHANGE NULL IN ADMIN ID TO LOGIC CHOOSING AN ADMIN
          , Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, report.getReporting_id());
      ps.setInt(2, report.getReported_id());
      ps.setString(3, report.getComment());
      ps.setTimestamp(4, new Timestamp(
          ZonedDateTime.of(report.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("report_id");
  }

  public void update(Report report)
  {
    String sql = "UPDATE Report SET status = ? WHERE report_id = ?";
    jdbcTemplate.update(sql, report.getStatus(), report.getId());
  }

  public void deleteById(int id)
  {
    String sql = "DELETE FROM Report WHERE report_id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class ReportRowMapper implements RowMapper<Report>
  {
    @Override public Report mapRow(ResultSet rs, int rowNum) throws SQLException
    {
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
