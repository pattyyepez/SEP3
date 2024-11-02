package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.Reports;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReportRepository {
  private final JdbcTemplate jdbcTemplate;

  public ReportRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Reports findById(int id) {
    String sql = "SELECT * FROM Report WHERE report_id = ?";
    return jdbcTemplate.queryForObject(sql, new ReportRowMapper(), id);
  }

  public List<Reports> findAll() {
    String sql = "SELECT * FROM Report";
    return jdbcTemplate.query(sql, new ReportRowMapper());
  }

  public int save(Reports report) {
    String sql = "INSERT INTO Report (owner_id, sitter_id, admin_id, comment, status, date) VALUES (?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql, report.getOwner_id(), report.getSitter_id(), report.getAdmin_id(),
        report.getComment(), report.getStatus(), new java.sql.Date(report.getDate().getTime()));
  }

  public int update(Reports report) {
    String sql = "UPDATE Report SET comment = ?, status = ? WHERE report_id = ?";
    return jdbcTemplate.update(sql, report.getComment(), report.getStatus(), report.getId());
  }

  public int deleteById(int id) {
    String sql = "DELETE FROM Report WHERE report_id = ?";
    return jdbcTemplate.update(sql, id);
  }

  private static class ReportRowMapper implements RowMapper<Reports> {
    @Override
    public Reports mapRow(ResultSet rs, int rowNum) throws SQLException {
      Reports report = new Reports();
      report.setId(rs.getInt("report_id"));
      report.setOwner_id(rs.getInt("owner_id"));
      report.setSitter_id(rs.getInt("sitter_id"));
      report.setAdmin_id(rs.getInt("admin_id"));
      report.setComment(rs.getString("comments"));
      report.setStatus(rs.getString("status"));
      report.setDate(rs.getDate("date"));
      return report;
    }
  }
}
