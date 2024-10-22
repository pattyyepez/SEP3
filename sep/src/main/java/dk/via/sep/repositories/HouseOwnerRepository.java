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

  public HouseOwner findById(Long id) {
    String sql = "SELECT * FROM HouseOwner WHERE owner_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseOwnerRowMapper(), id);
  }

  public void save(HouseOwner houseOwner) {
    String sql = "INSERT INTO HouseOwner (name, address, phone, email, isVerified) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, houseOwner.getName(), houseOwner.getAddress(), houseOwner.getPhone(), houseOwner.getEmail(), houseOwner.getVerified());
  }

  public void update(HouseOwner houseOwner) {
    String sql = "UPDATE HouseOwner SET name = ?, address = ?, phone = ?, email = ?, isVerified = ? WHERE owner_id = ?";
    jdbcTemplate.update(sql, houseOwner.getName(), houseOwner.getAddress(), houseOwner.getPhone(), houseOwner.getEmail(), houseOwner.getVerified(), houseOwner.getOwnerId());
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM HouseOwner WHERE owner_id = ?";
    jdbcTemplate.update(sql, id);
  }

  private static class HouseOwnerRowMapper implements RowMapper<HouseOwner> {
    @Override
    public HouseOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseOwner owner = new HouseOwner();
      owner.setOwnerId(rs.getLong("owner_id"));
      owner.setName(rs.getString("name"));
      owner.setAddress(rs.getString("address"));
      owner.setPhone(rs.getString("phone"));
      owner.setEmail(rs.getString("email"));
      owner.setVerified(rs.getBoolean("isVerified"));
      return owner;
    }
  }
}
