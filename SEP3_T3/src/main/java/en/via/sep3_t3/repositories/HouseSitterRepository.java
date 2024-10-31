package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseSitter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class HouseSitterRepository
{
  private final JdbcTemplate jdbcTemplate;

  public HouseSitterRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }
  public List<HouseSitter> findAll() {
    String sql = "SELECT *\n"
        + "FROM HouseSitter\n"
        + "JOIN Users U on U.id = HouseSitter.sitter_id";
    return jdbcTemplate.query(sql, new HouseSitterRepository.HouseSitterRowMapper());
  }
  public HouseSitter findById(int sitter_id) {
    String sql = "SELECT *\n"
        + "FROM HouseSitter\n"
        + "JOIN Users U on U.id = HouseSitter.sitter_id\n"
        + "WHERE sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseSitterRepository.HouseSitterRowMapper(), sitter_id);
  }

  public int save(HouseSitter houseSitter) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Users (email, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES\n"
              + "(?, ?, ?, ?, ?, FALSE, NULL)"

          , Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, houseSitter.getEmail());
      ps.setString(2, houseSitter.getPassword());
      ps.setString(3, houseSitter.getProfilePicture());
      ps.setString(4, houseSitter.getCPR());
      ps.setString(5, houseSitter.getPhone());
      return ps;
    }, keyHolder);

    int id = (int) keyHolder.getKeys().get("id");

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO HouseSitter (sitter_id, experience, biography) VALUES\n"
              + "(?, ?, ?)"

          , Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, id);
      ps.setString(2, houseSitter.getExperience());
      ps.setString(3, houseSitter.getBiography());
      return ps;
    });

    return id;
  }


  public void update(HouseSitter houseSitter) {
    String sql = "UPDATE Users\n"
        + "SET email = ?, password = ?, profile_picture = ?, CPR = ?, phone = ?, isVerified = ?, admin_id = ?\n"
        + "WHERE id = ?";

    jdbcTemplate.update(
        sql,
        houseSitter.getEmail(),
        houseSitter.getPassword(),
        houseSitter.getProfilePicture(),
        houseSitter.getCPR(),
        houseSitter.getPhone(),
        houseSitter.isVerified(),
        houseSitter.getAdminId() != 0 ? houseSitter.getAdminId() : null,
        houseSitter.getUserId());

    sql = "UPDATE houseSitter SET experience = ?, biography = ? WHERE sitter_id = ?";

    jdbcTemplate.update(
        sql,
        houseSitter.getExperience(),
        houseSitter.getBiography(),
        houseSitter.getUserId());
  }
  public void deleteById(int sitter_id) {
    String sql = "DELETE FROM HouseSitter WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitter_id);
    sql = "DELETE FROM Users WHERE id = ?";
    jdbcTemplate.update(sql, sitter_id);
  }

  private static class HouseSitterRowMapper implements RowMapper<HouseSitter>
  {
    @Override
    public HouseSitter mapRow(ResultSet rs, int rowNum) throws SQLException
    {
      HouseSitter houseSitter = new HouseSitter();

      houseSitter.setUserId(rs.getInt("sitter_id"));
      houseSitter.setEmail(rs.getString("email"));
      houseSitter.setPassword(rs.getString("password"));
      houseSitter.setProfilePicture(rs.getString("profile_picture"));
      houseSitter.setCPR(rs.getString("CPR"));
      houseSitter.setPhone(rs.getString("phone"));
      houseSitter.setVerified(rs.getBoolean("isVerified"));
      houseSitter.setAdminId(rs.getInt("admin_id"));
      houseSitter.setExperience(rs.getString("experience"));
      houseSitter.setBiography(rs.getString("biography"));

      return houseSitter;

    }
  }
}
