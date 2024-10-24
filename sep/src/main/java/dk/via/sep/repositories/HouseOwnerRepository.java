package dk.via.sep.repositories;

import dk.via.sep.domain.HouseOwner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HouseOwnerRepository {

  private final JdbcTemplate jdbcTemplate;

  public HouseOwnerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<HouseOwner> findAll() {
    String sql = "SELECT * FROM HouseOwner";
    return jdbcTemplate.query(sql, new HouseOwnerRowMapper());
  }

  public HouseOwner findById(int owner_id) {
    String sql = "SELECT * FROM HouseOwner WHERE owner_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseOwnerRowMapper(), owner_id);
  }

  public void save(HouseOwner houseOwner) {
    String sql = "INSERT INTO HouseOwner (owner_id, address, biography) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, houseOwner.getOwnerId(), houseOwner.getAddress(), houseOwner.getBiography());
  }

  public void update(HouseOwner houseOwner) {
    String sql = "UPDATE HouseOwner SET address = ?, biography = ? WHERE owner_id = ?";
    jdbcTemplate.update(sql, houseOwner.getAddress(), houseOwner.getBiography(), houseOwner.getOwnerId());
  }

  public void deleteById(int owner_id) {
    String sql = "DELETE FROM HouseOwner WHERE owner_id = ?";
    jdbcTemplate.update(sql, owner_id);
  }

  private static class HouseOwnerRowMapper implements RowMapper<HouseOwner> {
    @Override
    public HouseOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseOwner owner = new HouseOwner();
      owner.setOwnerId(rs.getInt("owner_id"));
      owner.setAddress(rs.getString("address"));
      owner.setBiography(rs.getString("biography"));
      return owner;
    }
  }
}
