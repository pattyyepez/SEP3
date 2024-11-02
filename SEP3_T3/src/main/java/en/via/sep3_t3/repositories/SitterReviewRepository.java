package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.SitterReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SitterReviewRepository {

  private final JdbcTemplate jdbcTemplate;

  public SitterReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public SitterReview findById(int id) {
    String sql = "SELECT * FROM Sitter_review WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, new SitterReviewRowMapper(), id);
  }

  public List<SitterReview> findAll() {
    String sql = "SELECT * FROM Sitter_review";
    return jdbcTemplate.query(sql, new SitterReviewRowMapper());
  }

  public void save(SitterReview sitterReview) {
    String sql = "INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        sitterReview.getOwner_id(),
        sitterReview.getSitter_id(),
        sitterReview.getRating(),
        sitterReview.getComment(),
        new java.sql.Date(sitterReview.getDate().getTime()));
  }

  public void update(SitterReview sitterReview) {
    String sql = "UPDATE Sitter_review SET owner_id = ?, sitter_id = ?, rating = ?, comments = ?, date = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        sitterReview.getOwner_id(),
        sitterReview.getSitter_id(),
        sitterReview.getRating(),
        sitterReview.getComment(),
        new java.sql.Date(sitterReview.getDate().getTime()),
        sitterReview.getId());
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM Sitter_review WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class SitterReviewRowMapper implements RowMapper<SitterReview> {
    @Override
    public SitterReview mapRow(ResultSet rs, int rowNum) throws SQLException {
      SitterReview sitterReview = new SitterReview();
      sitterReview.setId(rs.getInt("id"));
      sitterReview.setOwner_id(rs.getInt("owner_id"));
      sitterReview.setSitter_id(rs.getInt("sitter_id"));
      sitterReview.setRating(rs.getInt("rating"));
      sitterReview.setComment(rs.getString("comments"));
      sitterReview.setDate(rs.getDate("date"));
      return sitterReview;
    }
  }
}
