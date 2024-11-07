package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository public class HouseSitterRepository implements IHouseSitterRepository
{

  private final JdbcTemplate jdbcTemplate;

  public HouseSitterRepository(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<HouseSitter> findAll()
  {
    String sql = "SELECT * FROM HouseSitter JOIN Users U on U.id = HouseSitter.sitter_id";
    return jdbcTemplate.query(sql, new HouseSitterRowMapper());
  }

  public HouseSitter findById(int sitter_id)
  {
    String sql = "SELECT * FROM HouseSitter JOIN Users U on U.id = HouseSitter.sitter_id WHERE sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseSitterRowMapper(),
        sitter_id);
  }

  public int save(HouseSitter houseSitter)
  {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Users (email, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES (?, ?, ?, ?, ?, FALSE, NULL)",
          Statement.RETURN_GENERATED_KEYS);
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
          "INSERT INTO HouseSitter (sitter_id, past_experience, biography) VALUES (?, ?, ?)");
      ps.setInt(1, id);
      ps.setString(2, houseSitter.getExperience());
      ps.setString(3, houseSitter.getBiography());
      return ps;
    });

    saveSkills(houseSitter.getSkills(), id);
    savePictures(houseSitter.getPictures(), id);

    return id;
  }

  public void update(HouseSitter houseSitter)
  {
    String sql = "UPDATE Users SET email = ?, password = ?, profile_picture = ?, CPR = ?, phone = ?, isVerified = ?, admin_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, houseSitter.getEmail(), houseSitter.getPassword(),
        houseSitter.getProfilePicture(), houseSitter.getCPR(),
        houseSitter.getPhone(), houseSitter.isVerified(),
        houseSitter.getAdminId() != 0 ? houseSitter.getAdminId() : null,
        houseSitter.getUserId());

    sql = "UPDATE HouseSitter SET past_experience = ?, biography = ? WHERE sitter_id = ?";
    jdbcTemplate.update(sql, houseSitter.getExperience(), houseSitter.getBiography(), houseSitter.getUserId());

    deleteSkills(houseSitter.getUserId());
    saveSkills(houseSitter.getSkills(), houseSitter.getUserId());

    deletePictures(houseSitter.getUserId());
    savePictures(houseSitter.getPictures(), houseSitter.getUserId());
  }

  public void deleteById(int sitter_id)
  {
    deleteSkills(sitter_id);
    deletePictures(sitter_id);

    String sql = "DELETE FROM HouseSitter WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitter_id);

    sql = "DELETE FROM Users WHERE id = ?";
    jdbcTemplate.update(sql, sitter_id);
  }

  private void saveSkills(List<String> skills, int sitterId)
  {
    String sql = "INSERT INTO Sitter_skills (sitter_id, skill_id) VALUES (?, ?)";
    for (String skill : skills)
    {
      jdbcTemplate.update(sql, sitterId, getSkillId(skill));
    }
  }

  private int getSkillId(String skill)
  {
    String sql = "SELECT Skills.id FROM Skills WHERE type = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      return rs.getInt("id");
    }, skill).get(0);
  }

  private void deleteSkills(int sitterId)
  {
    String sql = "DELETE FROM Sitter_skills WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitterId);
  }

  private void savePictures(List<String> pictures, int sitterId)
  {
    String sql = "INSERT INTO SitterPictures (sitter_id, picture) VALUES (?, ?)";
    for (String picture : pictures)
    {
      jdbcTemplate.update(sql, sitterId, picture);
    }
  }

  private int getPictureId(String picture)
  {
    String sql = "SELECT sitter_id FROM SitterPictures WHERE picture = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      return rs.getInt("picture");
    }, picture).get(0);
  }

  private void deletePictures(int sitterId)
  {
    String sql = "DELETE FROM SitterPictures WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitterId);
  }

  private class HouseSitterRowMapper implements RowMapper<HouseSitter>
  {
    @Override public HouseSitter mapRow(ResultSet rs, int rowNum)
        throws SQLException
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
      houseSitter.setExperience(rs.getString("past_experience")); // Changed to past_experience
      houseSitter.setBiography(rs.getString("biography"));
      houseSitter.setPictures(fetchPictures(rs.getInt("sitter_id")));
      houseSitter.setSkills(fetchSkills(rs.getInt("sitter_id")));
      return houseSitter;
    }

    private ArrayList<String> fetchPictures(int sitterId)
    {
      String sql = "SELECT picture FROM Sitter_pictures WHERE sitter_id = ?";
      return (ArrayList<String>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        return rs.getString("picture");
      }, sitterId);
    }

    private ArrayList<String> fetchSkills(int sitterId)
    {
      String sql = "SELECT Skills.type FROM Sitter_skills JOIN Skills ON Skills.id = Sitter_skills.skill_id WHERE Sitter_skills.sitter_id = ?";
      return (ArrayList<String>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        return rs.getString("type");
      }, sitterId);
    }
  }
}
