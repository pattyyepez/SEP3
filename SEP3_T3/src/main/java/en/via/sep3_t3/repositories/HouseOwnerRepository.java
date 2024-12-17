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

/**
 * Repository implementation for managing {@link HouseOwner} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `HouseOwner` and `Users` tables to perform CRUD operations.</p>
 */
@Qualifier("HouseOwnerBase")
@Repository
public class HouseOwnerRepository implements IHouseOwnerRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code HouseOwnerRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public HouseOwnerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves all house owners from the database, including their user details.
   *
   * @return a list of all {@link HouseOwner} entities.
   */
  @Override
  public List<HouseOwner> findAll() {
    String sql = "SELECT * FROM HouseOwner JOIN Users U on U.id = HouseOwner.owner_id";
    return jdbcTemplate.query(sql, new HouseOwnerRowMapper());
  }

  /**
   * Retrieves a house owner by their owner ID, including their user details.
   *
   * @param owner_id the ID of the house owner.
   * @return the {@link HouseOwner} entity matching the given owner ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no owner is found with the given ID.
   */
  @Override
  public HouseOwner findById(int owner_id) {
    String sql = "SELECT * FROM HouseOwner JOIN Users U on U.id = HouseOwner.owner_id WHERE owner_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseOwnerRowMapper(), owner_id);
  }

  /**
   * Saves a new house owner to the database. This involves inserting records
   * into both the `Users` and `HouseOwner` tables.
   *
   * @param houseOwner the {@link HouseOwner} entity to save.
   * @return the ID of the newly created house owner.
   */
  @Override
  public int save(HouseOwner houseOwner) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO Users (name, email, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES (?, ?, ?, ?, ?, ?, FALSE, NULL)",
          Statement.RETURN_GENERATED_KEYS);
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
          "INSERT INTO HouseOwner (owner_id, address, biography) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, id);
      ps.setString(2, houseOwner.getAddress());
      ps.setString(3, houseOwner.getBiography());
      return ps;
    });

    return id;
  }

  /**
   * Updates an existing house owner in the database. Updates both `Users` and `HouseOwner` tables.
   *
   * @param houseOwner the {@link HouseOwner} entity to update.
   */
  @Override
  public void update(HouseOwner houseOwner) {
    String sql = "UPDATE Users SET name = ?, profile_picture = ?, CPR = ?, phone = ?, isVerified = ?, admin_id = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        houseOwner.getName(),
        houseOwner.getProfilePicture(),
        houseOwner.getCPR(),
        houseOwner.getPhone(),
        houseOwner.isVerified(),
        houseOwner.getAdminId() != 0 ? houseOwner.getAdminId() : null,
        houseOwner.getUserId());

    sql = "UPDATE HouseOwner SET address = ?, biography = ? WHERE owner_id = ?";
    jdbcTemplate.update(sql, houseOwner.getAddress(), houseOwner.getBiography(), houseOwner.getUserId());
  }

  /**
   * Deletes a house owner by their owner ID. Deletes from both `HouseOwner` and `Users` tables.
   *
   * @param owner_id the ID of the house owner to delete.
   */
  @Override
  public void deleteById(int owner_id) {
    String sql = "DELETE FROM HouseOwner WHERE owner_id = ?";
    jdbcTemplate.update(sql, owner_id);
    sql = "DELETE FROM Users WHERE id = ?";
    jdbcTemplate.update(sql, owner_id);
  }

  /**
   * Private static class for mapping {@link HouseOwner} rows from the database.
   */
  private static class HouseOwnerRowMapper implements RowMapper<HouseOwner> {

    /**
     * Maps a single row of the `HouseOwner` and `Users` tables to a {@link HouseOwner} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link HouseOwner} object.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public HouseOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
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


