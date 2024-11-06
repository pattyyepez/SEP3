package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.SitterReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

  public int save(SitterReview sitterReview) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, sitterReview.getOwner_id());
      ps.setInt(2, sitterReview.getSitter_id());
      ps.setInt(3, sitterReview.getRating());
      ps.setString(4, sitterReview.getComment());
      ps.setTimestamp(5, new Timestamp(sitterReview.getDate().getTime()));
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("id");
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
      sitterReview.setDate(rs.getTimestamp("date"));
      return sitterReview;
    }
  }
}
