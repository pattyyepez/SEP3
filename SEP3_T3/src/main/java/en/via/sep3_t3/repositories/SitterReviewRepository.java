package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.domain.SitterReview;
import en.via.sep3_t3.repositoryContracts.ISitterReviewRepository;
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

@Qualifier("SitterReviewBase")
@Repository
public class SitterReviewRepository implements ISitterReviewRepository
{

  private final JdbcTemplate jdbcTemplate;

  public SitterReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public SitterReview findById(int ownerId, int sitterId) {
    String sql = "SELECT * FROM Sitter_review WHERE owner_id = ? AND sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new SitterReviewRowMapper(), ownerId, sitterId);
  }

  public List<SitterReview> findAll() {
    String sql = "SELECT * FROM Sitter_review";
    return jdbcTemplate.query(sql, new SitterReviewRowMapper());
  }

  public void save(SitterReview sitterReview)
  {
    String sql = "INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, sitterReview.getOwner_id(), sitterReview.getSitter_id(),
        sitterReview.getRating(), sitterReview.getComment(),
        new Timestamp(ZonedDateTime.of(sitterReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  public SitterReview update(SitterReview sitterReview) {
    String sql = "UPDATE Sitter_review SET rating = ?, comments = ? WHERE owner_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, sitterReview.getRating(), sitterReview.getComment(),
        sitterReview.getOwner_id(), sitterReview.getSitter_id());
    return findById(sitterReview.getOwner_id(), sitterReview.getSitter_id());
  }

  public void deleteById(int ownerId, int sitterId) {
    String sql = "DELETE FROM Sitter_review WHERE owner_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, ownerId, sitterId);
  }

  private static class SitterReviewRowMapper implements RowMapper<SitterReview> {
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
