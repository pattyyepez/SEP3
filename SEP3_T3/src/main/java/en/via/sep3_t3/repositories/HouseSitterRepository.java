package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing {@link HouseSitter} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `HouseSitter`, `Users`, `Sitter_skills`, and `Sitter_pictures` tables
 * to perform CRUD operations on house sitters, including their associated skills and pictures.</p>
 */
@Qualifier("HouseSitterBase")
@Repository
public class HouseSitterRepository implements IHouseSitterRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code HouseSitterRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public HouseSitterRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves all house sitters from the database, including their associated user details, skills, and pictures.
   *
   * @return a list of all {@link HouseSitter} entities.
   */
  public List<HouseSitter> findAll() {
    String sql = "SELECT * FROM HouseSitter JOIN Users U on U.id = HouseSitter.sitter_id";
    return jdbcTemplate.query(sql, new HouseSitterRowMapper());
  }

  /**
   * Retrieves all skills from the database.
   *
   * @return a list of skill types as {@link String} objects.
   */
  @Override
  public List<String> findAllSkills() {
    String sql = "SELECT * FROM Skills";
    return jdbcTemplate.query(sql, new SkillRowMapper());
  }

  /**
   * Retrieves a house sitter by their sitter ID, including their associated user details, skills, and pictures.
   *
   * @param sitter_id the ID of the house sitter.
   * @return the {@link HouseSitter} entity matching the given sitter ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no sitter is found with the given ID.
   */
  public HouseSitter findById(int sitter_id) {
    String sql = "SELECT * FROM HouseSitter JOIN Users U on U.id = HouseSitter.sitter_id WHERE sitter_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseSitterRowMapper(), sitter_id);
  }

  /**
   * Saves a new {@link HouseSitter} to the database. This involves inserting records into both the `Users` and `HouseSitter` tables.
   *
   * @param houseSitter the {@link HouseSitter} entity to save.
   * @return the ID of the newly created house sitter.
   */
  public int save(HouseSitter houseSitter) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Users (name, email, password, profile_picture, CPR, phone, isVerified, admin_id)"
              + " VALUES (?, ?, ?, ?, ?, ?, FALSE, NULL)", Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, houseSitter.getName());
      ps.setString(2, houseSitter.getEmail());
      ps.setString(3, houseSitter.getPassword());
      ps.setString(4, houseSitter.getProfilePicture());
      ps.setString(5, houseSitter.getCPR());
      ps.setString(6, houseSitter.getPhone());
      return ps;
    }, keyHolder);

    int id = (int) keyHolder.getKeys().get("id");
    houseSitter.setUserId(id);

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

  /**
   * Updates an existing {@link HouseSitter} in the database, including updating the associated skills and pictures.
   *
   * @param houseSitter the {@link HouseSitter} entity to update.
   */
  public void update(HouseSitter houseSitter) {
    String sql = "UPDATE Users SET name = ?, profile_picture = ?, CPR = ?, phone = ?, isVerified = ?, admin_id = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        houseSitter.getName(), houseSitter.getProfilePicture(),
        houseSitter.getCPR(), houseSitter.getPhone(),
        houseSitter.isVerified(),
        houseSitter.getAdminId() != 0 ? houseSitter.getAdminId() : null,
        houseSitter.getUserId());

    sql = "UPDATE HouseSitter SET past_experience = ?, biography = ? WHERE sitter_id = ?";
    jdbcTemplate.update(sql, houseSitter.getExperience(), houseSitter.getBiography(), houseSitter.getUserId());

    deleteSkills(houseSitter.getUserId());
    saveSkills(houseSitter.getSkills(), houseSitter.getUserId());

    deletePictures(houseSitter.getUserId());
    savePictures(houseSitter.getPictures(), houseSitter.getUserId());
  }

  /**
   * Deletes a house sitter by their sitter ID, including their associated skills and pictures.
   *
   * @param sitter_id the ID of the house sitter to delete.
   */
  public void deleteById(int sitter_id) {
    deleteSkills(sitter_id);
    deletePictures(sitter_id);

    String sql = "DELETE FROM HouseSitter WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitter_id);

    sql = "DELETE FROM Users WHERE id = ?";
    jdbcTemplate.update(sql, sitter_id);
  }

  /**
   * Saves a list of skills for a house sitter.
   *
   * @param skills the list of skills to associate with the house sitter.
   * @param sitterId the ID of the house sitter.
   */
  private void saveSkills(List<String> skills, int sitterId) {
    String sql = "INSERT INTO Sitter_skills (sitter_id, skill_id) VALUES (?, ?)";
    for (String skill : skills) {
      jdbcTemplate.update(sql, sitterId, getSkillId(skill));
    }
  }

  /**
   * Retrieves the ID of a skill based on its type.
   *
   * @param skill the skill type.
   * @return the ID of the skill.
   */
  private int getSkillId(String skill) {
    String sql = "SELECT Skills.id FROM Skills WHERE type = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      return rs.getInt("id");
    }, skill).get(0);
  }

  /**
   * Deletes all skills associated with a house sitter.
   *
   * @param sitterId the ID of the house sitter.
   */
  private void deleteSkills(int sitterId) {
    String sql = "DELETE FROM Sitter_skills WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitterId);
  }

  /**
   * Saves a list of pictures for a house sitter.
   *
   * @param pictures the list of pictures to associate with the house sitter.
   * @param sitterId the ID of the house sitter.
   */
  private void savePictures(List<String> pictures, int sitterId) {
    String sql = "INSERT INTO Sitter_pictures (sitter_id, picture) VALUES (?, ?)";
    for (String picture : pictures) {
      jdbcTemplate.update(sql, sitterId, picture);
    }
  }

  /**
   * Deletes all pictures associated with a house sitter.
   *
   * @param sitterId the ID of the house sitter.
   */
  private void deletePictures(int sitterId) {
    String sql = "DELETE FROM Sitter_pictures WHERE sitter_id = ?";
    jdbcTemplate.update(sql, sitterId);
  }

  /**
   * Private static class for mapping {@link HouseSitter} rows from the database.
   */
  private class HouseSitterRowMapper implements RowMapper<HouseSitter> {

    /**
     * Maps a single row of the `HouseSitter` and `Users` tables to a {@link HouseSitter} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link HouseSitter} object.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public HouseSitter mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseSitter houseSitter = new HouseSitter();
      houseSitter.setUserId(rs.getInt("sitter_id"));
      houseSitter.setName(rs.getString("name"));
      houseSitter.setEmail(rs.getString("email"));
      houseSitter.setPassword(rs.getString("password"));
      houseSitter.setProfilePicture(rs.getString("profile_picture"));
      houseSitter.setCPR(rs.getString("CPR"));
      houseSitter.setPhone(rs.getString("phone"));
      houseSitter.setVerified(rs.getBoolean("isVerified"));
      houseSitter.setAdminId(rs.getInt("admin_id"));
      houseSitter.setExperience(rs.getString("past_experience"));
      houseSitter.setBiography(rs.getString("biography"));
      houseSitter.setPictures(fetchPictures(rs.getInt("sitter_id")));
      houseSitter.setSkills(fetchSkills(rs.getInt("sitter_id")));
      return houseSitter;
    }

    /**
     * Fetches the pictures associated with a house sitter.
     *
     * @param sitterId the ID of the house sitter.
     * @return a list of picture URLs associated with the sitter.
     */
    private ArrayList<String> fetchPictures(int sitterId) {
      String sql = "SELECT picture FROM Sitter_pictures WHERE sitter_id = ?";
      return (ArrayList<String>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        return rs.getString("picture");
      }, sitterId);
    }

    /**
     * Fetches the skills associated with a house sitter.
     *
     * @param sitterId the ID of the house sitter.
     * @return a list of skill types associated with the sitter.
     */
    private ArrayList<String> fetchSkills(int sitterId) {
      String sql = "SELECT Skills.type FROM Sitter_skills JOIN Skills ON Skills.id = Sitter_skills.skill_id WHERE Sitter_skills.sitter_id = ?";
      return (ArrayList<String>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        return rs.getString("type");
      }, sitterId);
    }
  }

  /**
   * Private static class for mapping skill rows from the database to {@link String} skill types.
   */
  private class SkillRowMapper implements RowMapper<String> {

    /**
     * Maps a single row of the `Skills` table to a skill type.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped skill type.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString("type");
    }
  }
}

