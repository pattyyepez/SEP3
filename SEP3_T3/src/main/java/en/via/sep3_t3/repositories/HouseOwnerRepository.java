package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseOwner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

  public int save(HouseOwner houseOwner) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement("INSERT INTO HouseOwner (address, biography) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, houseOwner.getAddress());
      ps.setString(2, houseOwner.getBiography());
      return ps;
    }, keyHolder);
    return (int) keyHolder.getKeys().get("owner_id");
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

