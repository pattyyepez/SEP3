package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseProfile;
import en.via.sep3_t3.domain.Amenity;
import en.via.sep3_t3.domain.Chore;
import en.via.sep3_t3.domain.Rule;
import en.via.sep3_t3.domain.HousePicture;
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
@Repository


public class HouseProfileRepository {


  private static JdbcTemplate jdbcTemplate = null;

  public HouseProfileRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<HouseProfile> findAll() {
    String sql = "SELECT * FROM HouseProfile";
    return jdbcTemplate.query(sql, new HouseProfileRowMapper());
  }

  public HouseProfile findById(int profile_id) {
    String sql = "SELECT * FROM HouseProfile WHERE profile_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseProfileRowMapper(), profile_id);
  }

  public int save(HouseProfile houseProfile) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO HouseProfile (owner_id, description, address, region, city) VALUES (?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseProfile.getOwner_id());
      ps.setString(2, houseProfile.getDescription());
      ps.setString(3, houseProfile.getAddress());
      ps.setString(4, houseProfile.getRegion());
      ps.setString(5, houseProfile.getCity());
      return ps;
    }, keyHolder);

    int profileId = (int) keyHolder.getKeys().get("profile_id");

    saveAmenities(houseProfile.getAmenities(), profileId);
    saveChores(houseProfile.getChores(), profileId);
    saveRules(houseProfile.getRules(), profileId);
    savePictures(houseProfile.getPictures(), profileId);

    return profileId;
  }

  public void update(HouseProfile houseProfile) {
    String sql = "UPDATE HouseProfile SET description = ?, address = ?, region = ?, city = ? WHERE profile_id = ?";
    jdbcTemplate.update(sql, houseProfile.getDescription(), houseProfile.getAddress(), houseProfile.getRegion(), houseProfile.getCity(), houseProfile.getOwner_id());

    deleteAmenities(houseProfile.getOwner_id());
    deleteChores(houseProfile.getOwner_id());
    deleteRules(houseProfile.getOwner_id());
    deletePictures(houseProfile.getOwner_id());

    saveAmenities(houseProfile.getAmenities(), houseProfile.getOwner_id());
    saveChores(houseProfile.getChores(), houseProfile.getOwner_id());
    saveRules(houseProfile.getRules(), houseProfile.getOwner_id());
    savePictures(houseProfile.getPictures(), houseProfile.getOwner_id());
  }

  public void deleteById(int profile_id) {
    deleteAmenities(profile_id);
    deleteChores(profile_id);
    deleteRules(profile_id);
    deletePictures(profile_id);

    String sql = "DELETE FROM HouseProfile WHERE profile_id = ?";
    jdbcTemplate.update(sql, profile_id);
  }

  private void saveAmenities(List<Amenity> amenities, int profileId) {
    String sql = "INSERT INTO House_amenities (profile_id, amenity_id) VALUES (?, ?)";
    for (Amenity amenity : amenities) {
      jdbcTemplate.update(sql, profileId, amenity.getId());
    }
  }

  private void deleteAmenities(int profileId) {
    String sql = "DELETE FROM House_amenities WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  private void saveChores(List<Chore> chores, int profileId) {
    String sql = "INSERT INTO House_chores (profile_id, chore_id) VALUES (?, ?)";
    for (Chore chore : chores) {
      jdbcTemplate.update(sql, profileId, chore.getId());
    }
  }

  private void deleteChores(int profileId) {
    String sql = "DELETE FROM House_chores WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  private void saveRules(List<Rule> rules, int profileId) {
    String sql = "INSERT INTO House_rules (profile_id, rule_id) VALUES (?, ?)";
    for (Rule rule : rules) {
      jdbcTemplate.update(sql, profileId, rule.getId());
    }
  }

  private void deleteRules(int profileId) {
    String sql = "DELETE FROM House_rules WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  private void savePictures(List<HousePicture> pictures, int profileId) {
    String sql = "INSERT INTO House_pictures (profile_id, picture) VALUES (?, ?)";
    for (HousePicture picture : pictures) {
      jdbcTemplate.update(sql, profileId, picture.getPath());
    }
  }

  private void deletePictures(int profileId) {
    String sql = "DELETE FROM House_pictures WHERE profile_id = ?";
    jdbcTemplate.update(sql, profileId);
  }

  private static class HouseProfileRowMapper implements RowMapper<HouseProfile> {
    @Override
    public HouseProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseProfile houseProfile = new HouseProfile();
      houseProfile.setOwner_id(rs.getInt("owner_id"));
      houseProfile.setDescription(rs.getString("description"));
      houseProfile.setAddress(rs.getString("address"));
      houseProfile.setRegion(rs.getString("region"));
      houseProfile.setCity(rs.getString("city"));
      houseProfile.setAmenities(fetchAmenities(rs.getInt("profile_id")));
      houseProfile.setChores(fetchChores(rs.getInt("profile_id")));
      houseProfile.setRules(fetchRules(rs.getInt("profile_id")));
      houseProfile.setPictures(fetchPictures(rs.getInt("profile_id")));
      return houseProfile;
    }

    private ArrayList<Amenity> fetchAmenities(int profileId) {
      String sql = "SELECT A.id, A.type FROM House_amenities HA JOIN Amenities A ON A.id = HA.amenity_id WHERE HA.profile_id = ?";
      return (ArrayList<Amenity>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        Amenity amenity = new Amenity();
        amenity.setId(rs.getInt("id"));
        amenity.setType(rs.getString("type"));
        return amenity;
      }, profileId);
    }

    private ArrayList<Chore> fetchChores(int profileId) {
      String sql = "SELECT C.id, C.type FROM House_chores HC JOIN Chores C ON C.id = HC.chore_id WHERE HC.profile_id = ?";
      return (ArrayList<Chore>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        Chore chore = new Chore();
        chore.setId(rs.getInt("id"));
        chore.setType(rs.getString("type"));
        return chore;
      }, profileId);
    }

    private ArrayList<Rule> fetchRules(int profileId) {
      String sql = "SELECT R.id, R.type FROM House_rules HR JOIN Rules R ON R.id = HR.rule_id WHERE HR.profile_id = ?";
      return (ArrayList<Rule>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        Rule rule = new Rule();
        rule.setId(rs.getInt("id"));
        rule.setType(rs.getString("type"));
        return rule;
      }, profileId);
    }

    private ArrayList<HousePicture> fetchPictures(int profileId) {
      String sql = "SELECT picture AS path FROM House_pictures WHERE profile_id = ?";
      return (ArrayList<HousePicture>) jdbcTemplate.query(sql, (rs, rowNum) -> {
        HousePicture picture = new HousePicture();
        picture.setPath(rs.getString("path"));
        return picture;
      }, profileId);
    }

  }
}
