package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository public class HouseReviewRepository implements IHouseReviewRepository
{

  private final JdbcTemplate jdbcTemplate;

  public HouseReviewRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }

  public HouseReview findById(int id)
  {
    String sql = "SELECT * FROM House_review WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseReviewRowMapper(), id);
  }

  public List<HouseReview> findAll()
  {
    String sql = "SELECT * FROM House_review";
    return jdbcTemplate.query(sql, new HouseReviewRowMapper());
  }

  public int save(HouseReview houseReview)
  {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseReview.getProfile_id());
      ps.setInt(2, houseReview.getSitter_id());
      ps.setInt(3, houseReview.getRating());
      ps.setString(4, houseReview.getComment());
      ps.setTimestamp(5, new Timestamp(houseReview.getDate().getTime()));
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("id");
  }

  public void deleteById(int id)
  {
    String sql = "DELETE FROM House_review WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class HouseReviewRowMapper implements RowMapper<HouseReview>
  {
    @Override public HouseReview mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
      HouseReview houseReview = new HouseReview();
      houseReview.setId(rs.getInt("id"));
      houseReview.setProfile_id(rs.getInt("profile_id"));
      houseReview.setSitter_id(rs.getInt("sitter_id"));
      houseReview.setRating(rs.getInt("rating"));
      houseReview.setComment(rs.getString("comments"));
      houseReview.setDate(rs.getTimestamp("date"));
      return houseReview;
    }
  }
}
