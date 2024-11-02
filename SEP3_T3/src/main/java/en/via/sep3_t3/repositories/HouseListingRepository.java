package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseListing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class HouseListingRepository {

  private final JdbcTemplate jdbcTemplate;

  public HouseListingRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<HouseListing> findAll() {
    String sql = "SELECT * FROM House_listing";
    return jdbcTemplate.query(sql, new HouseListingRowMapper());
  }

  public HouseListing findById(int id) {
    String sql = "SELECT * FROM House_listing WHERE listing_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseListingRowMapper(), id);
  }

  public int save(HouseListing houseListing) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO House_listing (profile_id, startDate, endDate, status) VALUES (?, ?, ?, ?)";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseListing.getProfile_id());
      ps.setDate(2, new java.sql.Date(houseListing.getStartDate().getTime()));
      ps.setDate(3, new java.sql.Date(houseListing.getEndDate().getTime()));
      ps.setString(4, houseListing.getStatus());
      return ps;
    }, keyHolder);

    return keyHolder.getKey().intValue();
  }

  public void update(HouseListing houseListing) {
    String sql = "UPDATE House_listing SET profile_id = ?, startDate = ?, endDate = ?, status = ? WHERE listing_id = ?";
    jdbcTemplate.update(sql, houseListing.getProfile_id(), new java.sql.Date(houseListing.getStartDate().getTime()),
        new java.sql.Date(houseListing.getEndDate().getTime()), houseListing.getStatus(), houseListing.getId());
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM House_listing WHERE listing_id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class HouseListingRowMapper implements RowMapper<HouseListing> {
    @Override
    public HouseListing mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseListing houseListing = new HouseListing();
      houseListing.setId(rs.getInt("listing_id"));
      houseListing.setProfile_id(rs.getInt("profile_id"));
      houseListing.setStartDate(rs.getDate("startDate"));
      houseListing.setEndDate(rs.getDate("endDate"));
      houseListing.setStatus(rs.getString("status"));
      return houseListing;
    }
  }
}
