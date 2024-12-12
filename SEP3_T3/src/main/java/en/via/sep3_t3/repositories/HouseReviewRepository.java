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

@Qualifier("HouseReviewBase")
@Repository
public class HouseReviewRepository implements IHouseReviewRepository
{

  private final JdbcTemplate jdbcTemplate;

  public HouseReviewRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }

  public HouseReview findById(int profileId, int sitterId)
  {
    String sql = "SELECT * FROM House_review WHERE profile_id = ? AND sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseReviewRowMapper(), profileId, sitterId);
  }

  public List<HouseReview> findAll()
  {
    String sql = "SELECT * FROM House_review";
    return jdbcTemplate.query(sql, new HouseReviewRowMapper());
  }

  public void save(HouseReview houseReview)
  {
    String sql = "INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, houseReview.getProfile_id(), houseReview.getSitter_id(),
        houseReview.getRating(), houseReview.getComment(),
        new Timestamp(ZonedDateTime.of(houseReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()));
  }

  public HouseReview update(HouseReview houseReview) {
    String sql = "UPDATE House_review SET rating = ?, comments = ?, date = ? WHERE profile_id = ? AND sitter_id = ?";
    jdbcTemplate.update(sql, houseReview.getRating(), houseReview.getComment(),
        new Timestamp(ZonedDateTime.of(houseReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()),
        houseReview.getProfile_id(), houseReview.getSitter_id());
    return findById(houseReview.getProfile_id(), houseReview.getSitter_id());
  }

  public void deleteById(int profileId, int sitterId)
  {
    String sql = "DELETE FROM House_review WHERE id = ?";
    jdbcTemplate.update(sql, profileId, sitterId);
  }

  private static class HouseReviewRowMapper implements RowMapper<HouseReview>
  {
    @Override public HouseReview mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
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
