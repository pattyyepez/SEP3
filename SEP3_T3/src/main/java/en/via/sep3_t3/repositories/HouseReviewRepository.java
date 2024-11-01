package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HouseReviewRepository {

  private final JdbcTemplate jdbcTemplate;

  public HouseReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public HouseReview findById(int id) {
    String sql = "SELECT * FROM House_review WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseReviewRowMapper(), id);
  }

  public List<HouseReview> findAll() {
    String sql = "SELECT * FROM House_review";
    return jdbcTemplate.query(sql, new HouseReviewRowMapper());
  }

  public int save(HouseReview houseReview) {
    String sql = "INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql, houseReview.getProfile_id(), houseReview.getSitter_id(),
        houseReview.getRating(), houseReview.getComment(), new java.sql.Date(houseReview.getDate().getTime()));
  }

  public void update(HouseReview houseReview) {
    String sql = "UPDATE House_review SET profile_id = ?, sitter_id = ?, rating = ?, comments = ?, date = ? WHERE id = ?";
    jdbcTemplate.update(sql, houseReview.getProfile_id(), houseReview.getSitter_id(),
        houseReview.getRating(), houseReview.getComment(), new java.sql.Date(houseReview.getDate().getTime()), houseReview.getId());
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM House_review WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class HouseReviewRowMapper implements RowMapper<HouseReview> {
    @Override
    public HouseReview mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseReview houseReview = new HouseReview();
      houseReview.setId(rs.getInt("id"));
      houseReview.setProfile_id(rs.getInt("profile_id"));
      houseReview.setSitter_id(rs.getInt("sitter_id"));
      houseReview.setRating(rs.getInt("rating"));
      houseReview.setComment(rs.getString("comments"));
      houseReview.setDate(rs.getDate("date"));
      return houseReview;
    }
  }
}
