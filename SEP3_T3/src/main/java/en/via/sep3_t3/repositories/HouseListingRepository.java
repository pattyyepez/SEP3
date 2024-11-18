package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositoryContracts.IHouseListingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Qualifier("HouseListingBase")
@Repository
public class HouseListingRepository implements IHouseListingRepository
{

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
    String sql = "INSERT INTO House_listing (profile_id, startDate, endDate, status) VALUES (?, ?, ?, 'Open')";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseListing.getProfile_id());
      ps.setTimestamp(2, new Timestamp(houseListing.getStartDate().getTime()));
      ps.setTimestamp(3, new Timestamp(houseListing.getEndDate().getTime()));
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("listing_id");
  }

  public HouseListing update(HouseListing houseListing) {
    String sql = "UPDATE House_listing SET status = ? WHERE listing_id = ?";
    jdbcTemplate.update(sql, houseListing.getStatus(), houseListing.getId());

    return findById(houseListing.getId());
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
      houseListing.setStartDate(rs.getTimestamp("startDate"));
      houseListing.setEndDate(rs.getTimestamp("endDate"));
      houseListing.setStatus(rs.getString("status"));
      return houseListing;
    }
  }
}
