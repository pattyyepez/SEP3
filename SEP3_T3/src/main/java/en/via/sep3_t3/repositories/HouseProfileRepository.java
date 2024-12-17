package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseProfile;
import en.via.sep3_t3.repositoryContracts.IHouseProfileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository implementation for managing {@link HouseProfile} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `HouseProfile`, `Amenities`, `Chores`, `Rules`, and related junction tables
 * to manage house profiles and their associated data.</p>
 */
@Qualifier("HouseProfileBase")
@Repository
public class HouseProfileRepository implements IHouseProfileRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code HouseProfileRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public HouseProfileRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves all house profiles from the database.
   *
   * @return a list of all {@link HouseProfile} entities.
   */
  @Override
  public List<HouseProfile> findAll() {
    String sql = "SELECT * FROM HouseProfile";
    return jdbcTemplate.query(sql, new HouseProfileRowMapper());
  }

  /**
   * Retrieves all rules from the `Rules` table.
   *
   * @return a list of all rule descriptions.
   */
  @Override
  public List<String> findAllRules() {
    String sql = "SELECT * FROM Rules";
    return jdbcTemplate.query(sql, new RulesRowMapper());
  }

  /**
   * Retrieves all chores from the `Chores` table.
   *
   * @return a list of all chore descriptions.
   */
  @Override
  public List<String> findAllChores() {
    String sql = "SELECT * FROM Chores";
    return jdbcTemplate.query(sql, new ChoresRowMapper());
  }

  /**
   * Retrieves all amenities from the `Amenities` table.
   *
   * @return a list of all amenity descriptions.
   */
  @Override
  public List<String> findAllAmenities() {
    String sql = "SELECT * FROM Amenities";
    return jdbcTemplate.query(sql, new AmenitiesRowMapper());
  }

  /**
   * Finds a house profile by its ID.
   *
   * @param id the ID of the house profile.
   * @return the {@link HouseProfile} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no house profile is found with the given ID.
   */
  @Override
  public HouseProfile findById(int id) {
    String sql = "SELECT * FROM HouseProfile WHERE profile_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseProfileRowMapper(), id);
  }

  /**
   * Saves a new house profile to the database and inserts related data into associated tables.
   *
   * @param houseProfile the {@link HouseProfile} entity to save.
   * @return the ID of the newly created house profile.
   */
  @Override
  public int save(HouseProfile houseProfile) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO HouseProfile (owner_id, title, description, address, region, city) VALUES (?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseProfile.getOwner_id());
      ps.setString(2, houseProfile.getTitle());
      ps.setString(3, houseProfile.getDescription());
      ps.setString(4, houseProfile.getAddress());
      ps.setString(5, houseProfile.getRegion());
      ps.setString(6, houseProfile.getCity());
      return ps;
    }, keyHolder);

    int profileId = (int) keyHolder.getKeys().get("profile_id");

    saveAmenities(houseProfile.getAmenities(), profileId);
    saveChores(houseProfile.getChores(), profileId);
    saveRules(houseProfile.getRules(), profileId);
    savePictures(houseProfile.getPictures(), profileId);

    return profileId;
  }

  /**
   * Updates an existing house profile in the database and replaces related data in associated tables.
   *
   * @param houseProfile the {@link HouseProfile} entity to update.
   * @return the updated {@link HouseProfile}.
   */
  @Override
  public HouseProfile update(HouseProfile houseProfile) {
    String sql = "UPDATE HouseProfile SET title = ?, description = ?, address = ?, region = ?, city = ? WHERE profile_id = ?";
    jdbcTemplate.update(sql,
        houseProfile.getTitle(), houseProfile.getDescription(),
        houseProfile.getAddress(), houseProfile.getRegion(),
        houseProfile.getCity(), houseProfile.getId());

    deleteAmenities(houseProfile.getId());
    deleteChores(houseProfile.getId());
    deleteRules(houseProfile.getId());
    deletePictures(houseProfile.getId());

    saveAmenities(houseProfile.getAmenities(), houseProfile.getId());
    saveChores(houseProfile.getChores(), houseProfile.getId());
    saveRules(houseProfile.getRules(), houseProfile.getId());
    savePictures(houseProfile.getPictures(), houseProfile.getId());

    return findById(houseProfile.getId());
  }

  /**
   * Deletes a house profile by its ID along with its related data.
   *
   * @param id the ID of the house profile to delete.
   */
  @Override
  public void deleteById(int id) {
    deleteAmenities(id);
    deleteChores(id);
    deleteRules(id);
    deletePictures(id);

    String sql = "DELETE FROM HouseProfile WHERE profile_id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Saves amenities associated with a house profile.
   *
   * @param amenities the list of amenities to save.
   * @param profileId the ID of the house profile to associate.
   */
  private void saveAmenities(List<String> amenities, int profileId) {
    if (!(amenities.size() == 1 && amenities.contains(""))) {
      String sql = "INSERT INTO House_amenities (profile_id, amenity_id) VALUES (?, ?)";
      for (String amenity : amenities) {
        jdbcTemplate.update(sql, profileId, getAmenityId(amenity));
      }
    }
  }

  /**
   * Deletes all amenities associated with a house profile.
   *
   * @param profileId the ID of the house profile.
   */
  private void deleteAmenities(int profileId) {
    String sql = "DELETE FROM House_amenities WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  /**
   * Saves chores associated with a house profile.
   *
   * @param chores the list of chores to save.
   * @param profileId the ID of the house profile to associate.
   */
  private void saveChores(List<String> chores, int profileId) {
    if (!(chores.size() == 1 && chores.contains(""))) {
      String sql = "INSERT INTO House_chores (profile_id, chore_id) VALUES (?, ?)";
      for (String chore : chores) {
        jdbcTemplate.update(sql, profileId, getChoreId(chore));
      }
    }
  }

  /**
   * Deletes all chores associated with a house profile.
   *
   * @param profileId the ID of the house profile.
   */
  private void deleteChores(int profileId) {
    String sql = "DELETE FROM House_chores WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  /**
   * Saves rules associated with a house profile.
   *
   * @param rules the list of rules to save.
   * @param profileId the ID of the house profile to associate.
   */
  private void saveRules(List<String> rules, int profileId) {
    if (!(rules.size() == 1 && rules.contains(""))) {
      String sql = "INSERT INTO House_rules (profile_id, rule_id) VALUES (?, ?)";
      for (String rule : rules) {
        jdbcTemplate.update(sql, profileId, getRuleId(rule));
      }
    }
  }

  /**
   * Deletes all rules associated with a house profile.
   *
   * @param profileId the ID of the house profile.
   */
  private void deleteRules(int profileId) {
    String sql = "DELETE FROM House_rules WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  /**
   * Saves pictures associated with a house profile.
   *
   * @param pictures the list of pictures to save.
   * @param profileId the ID of the house profile to associate.
   */
  private void savePictures(List<String> pictures, int profileId) {
    String sql = "INSERT INTO House_pictures (profile_id, picture) VALUES (?, ?)";
    for (String picture : pictures) {
      jdbcTemplate.update(sql, profileId, picture);
    }
  }

  /**
   * Deletes all pictures associated with a house profile.
   *
   * @param profileId the ID of the house profile.
   */
  private void deletePictures(int profileId) {
    String sql = "DELETE FROM House_pictures WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  /**
   * Retrieves the ID of an amenity based on its description.
   *
   * @param amenity the description of the amenity.
   * @return the ID of the amenity.
   */
  private int getAmenityId(String amenity) {
    String sql = "SELECT id FROM Amenities WHERE type = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, amenity);
  }

  /**
   * Retrieves the ID of a chore based on its description.
   *
   * @param chore the description of the chore.
   * @return the ID of the chore.
   */
  private int getChoreId(String chore) {
    String sql = "SELECT id FROM Chores WHERE type = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, chore);
  }

  /**
   * Retrieves the ID of a rule based on its description.
   *
   * @param rule the description of the rule.
   * @return the ID of the rule.
   */
  private int getRuleId(String rule) {
    String sql = "SELECT id FROM Rules WHERE type = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, rule);
  }

  /**
   * Maps rows from the `HouseProfile` table to {@link HouseProfile} entities.
   *
   * <p>This class is responsible for converting a single row from the `HouseProfile` table into
   * a complete {@link HouseProfile} object. It also populates the associated fields such as
   * amenities, chores, rules, and pictures by using fetch methods that query related tables.</p>
   */
  private class HouseProfileRowMapper implements RowMapper<HouseProfile> {

    /**
     * Maps a single row of data from the `HouseProfile` table into a {@link HouseProfile} object.
     *
     * @param rs     the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row being processed.
     * @return a fully populated {@link HouseProfile} object.
     * @throws SQLException if there is an issue accessing the row data.
     */
    @Override
    public HouseProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseProfile houseProfile = new HouseProfile();
      houseProfile.setId(rs.getInt("profile_id"));
      houseProfile.setTitle(rs.getString("title"));
      houseProfile.setDescription(rs.getString("description"));
      houseProfile.setCity(rs.getString("city"));
      houseProfile.setOwner_id(rs.getInt("owner_id"));
      houseProfile.setAddress(rs.getString("address"));
      houseProfile.setRegion(rs.getString("region"));
      houseProfile.setAmenities(fetchAmenities(rs.getInt("profile_id")));
      houseProfile.setChores(fetchChores(rs.getInt("profile_id")));
      houseProfile.setRules(fetchRules(rs.getInt("profile_id")));
      houseProfile.setPictures(fetchPictures(rs.getInt("profile_id")));
      return houseProfile;
    }

    /**
     * Fetches the list of amenities associated with a given house profile.
     *
     * @param profileId the ID of the house profile.
     * @return a list of amenity descriptions as {@link String}.
     */
    private List<String> fetchAmenities(int profileId) {
      String sql = "SELECT type FROM Amenities JOIN House_amenities ON id = amenity_id WHERE profile_id = ?";
      return jdbcTemplate.queryForList(sql, String.class, profileId);
    }

    /**
     * Fetches the list of chores associated with a given house profile.
     *
     * @param profileId the ID of the house profile.
     * @return a list of chore descriptions as {@link String}.
     */
    private List<String> fetchChores(int profileId) {
      String sql = "SELECT type FROM Chores JOIN House_chores ON id = chore_id WHERE profile_id = ?";
      return jdbcTemplate.queryForList(sql, String.class, profileId);
    }

    /**
     * Fetches the list of rules associated with a given house profile.
     *
     * @param profileId the ID of the house profile.
     * @return a list of rule descriptions as {@link String}.
     */
    private List<String> fetchRules(int profileId) {
      String sql = "SELECT type FROM Rules JOIN House_rules ON id = rule_id WHERE profile_id = ?";
      return jdbcTemplate.queryForList(sql, String.class, profileId);
    }

    /**
     * Fetches the list of pictures associated with a given house profile.
     *
     * @param profileId the ID of the house profile.
     * @return a list of picture URLs or paths as {@link String}.
     */
    private List<String> fetchPictures(int profileId) {
      String sql = "SELECT picture FROM House_pictures WHERE profile_id = ?";
      return jdbcTemplate.queryForList(sql, String.class, profileId);
    }
  }

  /**
   * Maps rows from the `Amenities` table to amenity descriptions.
   *
   * <p>This class maps a single row from the `Amenities` table into a {@link String}
   * representing the description of the amenity (e.g., "WiFi", "Pool").</p>
   */
  private class AmenitiesRowMapper implements RowMapper<String> {

    /**
     * Maps a single row of data from the `Amenities` table into a {@link String}.
     *
     * @param rs     the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row being processed.
     * @return the description of the amenity as a {@link String}.
     * @throws SQLException if there is an issue accessing the row data.
     */
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString("type");
    }
  }

  /**
   * Maps rows from the `Chores` table to chore descriptions.
   *
   * <p>This class maps a single row from the `Chores` table into a {@link String}
   * representing the description of the chore (e.g., "Vacuuming", "Dishwashing").</p>
   */
  private class ChoresRowMapper implements RowMapper<String> {

    /**
     * Maps a single row of data from the `Chores` table into a {@link String}.
     *
     * @param rs     the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row being processed.
     * @return the description of the chore as a {@link String}.
     * @throws SQLException if there is an issue accessing the row data.
     */
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString("type");
    }
  }

  /**
   * Maps rows from the `Rules` table to rule descriptions.
   *
   * <p>This class maps a single row from the `Rules` table into a {@link String}
   * representing the description of the rule (e.g., "No smoking", "Quiet hours after 10 PM").</p>
   */
  private class RulesRowMapper implements RowMapper<String> {

    /**
     * Maps a single row of data from the `Rules` table into a {@link String}.
     *
     * @param rs     the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row being processed.
     * @return the description of the rule as a {@link String}.
     * @throws SQLException if there is an issue accessing the row data.
     */
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString("type");
    }
  }

}

