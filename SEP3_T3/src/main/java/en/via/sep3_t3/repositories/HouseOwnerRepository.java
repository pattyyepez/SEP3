package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.repositoryContracts.IHouseOwnerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Qualifier("HouseOwnerBase")
@Repository
public class HouseOwnerRepository implements IHouseOwnerRepository
{

  private final JdbcTemplate jdbcTemplate;

  public HouseOwnerRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<HouseOwner> findAll()
  {
    String sql = "SELECT *\n" + "FROM HouseOwner\n"
        + "JOIN Users U on U.id = HouseOwner.owner_id";
    return jdbcTemplate.query(sql, new HouseOwnerRowMapper());
  }

  public HouseOwner findById(int owner_id)
  {
    String sql = "SELECT *\n" + "FROM HouseOwner\n"
        + "JOIN Users U on U.id = HouseOwner.owner_id\n" + "WHERE owner_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseOwnerRowMapper(), owner_id);
  }

  public int save(HouseOwner houseOwner)
  {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Users (name, email, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES\n"
              + "(?, ?, ?, ?, ?, ?, FALSE, NULL)"

          , Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, houseOwner.getName());
      ps.setString(2, houseOwner.getEmail());
      ps.setString(3, houseOwner.getPassword());
      ps.setString(4, houseOwner.getProfilePicture());
      ps.setString(5, houseOwner.getCPR());
      ps.setString(6, houseOwner.getPhone());
      return ps;
    }, keyHolder);

    int id = (int) keyHolder.getKeys().get("id");

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO HouseOwner (owner_id, address, biography) VALUES\n"
              + "(?, ?, ?)"

          , Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, id);
      ps.setString(2, houseOwner.getAddress());
      ps.setString(3, houseOwner.getBiography());
      return ps;
    });

    return id;
  }

  public void update(HouseOwner houseOwner)
  {
    String sql = "UPDATE Users\n"
        + "SET name = ?, email = ?, password = ?, profile_picture = ?, CPR = ?, phone = ?, isVerified = ?, admin_id = ?\n"
        + "WHERE id = ?";

    jdbcTemplate.update(sql,
        houseOwner.getName(),
        houseOwner.getEmail(),
        houseOwner.getPassword(),
        houseOwner.getProfilePicture(),
        houseOwner.getCPR(),
        houseOwner.getPhone(),
        houseOwner.isVerified(),
        houseOwner.getAdminId() != 0 ? houseOwner.getAdminId() : null,
        houseOwner.getUserId());

    sql = "UPDATE HouseOwner SET address = ?, biography = ? WHERE owner_id = ?";

    jdbcTemplate.update(sql, houseOwner.getAddress(), houseOwner.getBiography(),
        houseOwner.getUserId());
  }

  public void deleteById(int owner_id)
  {
    String sql = "DELETE FROM HouseOwner WHERE owner_id = ?";
    jdbcTemplate.update(sql, owner_id);
    sql = "DELETE FROM Users WHERE id = ?";
    jdbcTemplate.update(sql, owner_id);
  }

  private static class HouseOwnerRowMapper implements RowMapper<HouseOwner>
  {
    @Override public HouseOwner mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
      HouseOwner houseOwner = new HouseOwner();

      houseOwner.setUserId(rs.getInt("owner_id"));
      houseOwner.setName(rs.getString("name"));
      houseOwner.setEmail(rs.getString("email"));
      houseOwner.setPassword(rs.getString("password"));
      houseOwner.setProfilePicture(rs.getString("profile_picture"));
      houseOwner.setCPR(rs.getString("CPR"));
      houseOwner.setPhone(rs.getString("phone"));
      houseOwner.setVerified(rs.getBoolean("isVerified"));
      houseOwner.setAdminId(rs.getInt("admin_id"));
      houseOwner.setAddress(rs.getString("address"));
      houseOwner.setBiography(rs.getString("biography"));

      return houseOwner;

    }
  }
}

