package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.Report;

import java.util.List;

/**
 * Repository interface for managing {@link Report} entities.
 * Provides methods to perform CRUD operations on reports, such as retrieving, saving, updating, and deleting.
 */
public interface IReportRepository {

  /**
   * Retrieves all {@link Report} entities.
   *
   * @return a list of all {@link Report} entities.
   */
  List<Report> findAll();

  /**
   * Retrieves a specific {@link Report} by its ID.
   *
   * @param id the ID of the report.
   * @return the {@link Report} entity matching the given ID.
   * @throws org.springframework.dao.EmptyResultDataAccessException if no report is found with the given ID.
   */
  Report findById(int id);

  /**
   * Saves a new {@link Report} to the database.
   *
   * @param report the {@link Report} entity to save.
   * @return the ID of the newly saved report.
   */
  int save(Report report);

  /**
   * Updates an existing {@link Report} in the database.
   *
   * @param report the {@link Report} entity with updated information.
   */
  void update(Report report);

  /**
   * Deletes a {@link Report} by its ID.
   *
   * @param id the ID of the report to delete.
   */
  void deleteById(int id);
}

