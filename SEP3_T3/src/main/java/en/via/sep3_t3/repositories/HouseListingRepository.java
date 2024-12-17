package en.via.sep3_t3.repositories;

import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositoryContracts.IHouseListingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * Repository implementation for managing {@link HouseListing} entities.
 * Provides database access operations using {@link JdbcTemplate}.
 *
 * <p>This repository interacts with the `House_listing` table to perform CRUD operations.</p>
 */
@Qualifier("HouseListingBase")
@Repository
public class HouseListingRepository implements IHouseListingRepository {

  /**
   * The {@link JdbcTemplate} instance used for database interactions.
   */
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructs a new {@code HouseListingRepository} with the specified {@link JdbcTemplate}.
   *
   * @param jdbcTemplate the {@link JdbcTemplate} for executing SQL queries.
   */
  public HouseListingRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves all house listings from the database, ordered by start date.
   *
   * @return a list of all {@link HouseListing} entities.
   */
  @Override
  public List<HouseListing> findAll() {
    String sql = "SELECT * FROM House_listing ORDER BY startDate";
    return jdbcTemplate.query(sql, new HouseListingRowMapper());
  }

  /**
   * Retrieves a house listing by its ID.
   *
   * @param id the ID of the house listing.
   * @return the {@link HouseListing} matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no listing is found with the given ID.
   */
  @Override
  public HouseListing findById(int id) {
    String sql = "SELECT * FROM House_listing WHERE listing_id = ?";
    return jdbcTemplate.queryForObject(sql, new HouseListingRowMapper(), id);
  }

  /**
   * Saves a new house listing to the database.
   *
   * @param houseListing the {@link HouseListing} entity to save.
   * @return the ID of the newly created house listing.
   */
  @Override
  public int save(HouseListing houseListing) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO House_listing (profile_id, startDate, endDate, status) VALUES (?, ?, ?, ?)";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, houseListing.getProfile_id());
      ps.setTimestamp(2, new Timestamp(houseListing.getStartDate().getTime()));
      ps.setTimestamp(3, new Timestamp(houseListing.getEndDate().getTime()));
      ps.setString(4, houseListing.getStatus());
      return ps;
    }, keyHolder);

    return (int) keyHolder.getKeys().get("listing_id");
  }

  /**
   * Updates the status of an existing house listing in the database.
   *
   * @param houseListing the {@link HouseListing} entity to update.
   * @return the updated {@link HouseListing} entity.
   */
  @Override
  public HouseListing update(HouseListing houseListing) {
    String sql = "UPDATE House_listing SET status = ? WHERE listing_id = ?";
    jdbcTemplate.update(sql, houseListing.getStatus(), houseListing.getId());

    return findById(houseListing.getId());
  }

  /**
   * Deletes a house listing by its ID.
   *
   * @param id the ID of the house listing to delete.
   */
  @Override
  public void deleteById(int id) {
    String sql = "DELETE FROM House_listing WHERE listing_id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Private static class for mapping {@link HouseListing} rows from the database.
   */
  private static class HouseListingRowMapper implements RowMapper<HouseListing> {

    /**
     * Maps a single row of the `House_listing` table to a {@link HouseListing} object.
     *
     * @param rs the {@link ResultSet} containing the row data.
     * @param rowNum the number of the current row.
     * @return the mapped {@link HouseListing} object.
     * @throws SQLException if an SQL error occurs while reading the row.
     */
    @Override
    public HouseListing mapRow(ResultSet rs, int rowNum) throws SQLException {
      HouseListing houseListing = new HouseListing();
      houseListing.setId(rs.getInt("listing_id"));
      houseListing.setProfile_id(rs.getInt("profile_id"));
      houseListing.setStartDate(rs.getTimestamp("startDate"));
      houseListing.setEndDate(rs.getTimestamp("endDate"));
      houseListing.setStatus(rs.getString("status"));
      return houseListing;
    }
  }
}

