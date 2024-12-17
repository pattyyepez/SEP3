package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

/**
 * Represents a report in the system, used to track reports made by users
 * about other users in case of severe abuses by either of the two types of users.
 */
public class Report {

  /**
   * The unique ID of the report.
   */
  @Id
  private int id;

  /**
   * The ID of the user who filed the report.
   */
  private int reporting_id;

  /**
   * The ID of the user who is being reported.
   */
  private int reported_id;

  /**
   * The ID of the admin who handled the report.
   */
  private int admin_id;

  /**
   * A comment or description that provides additional information about the report.
   */
  private String comment;

  /**
   * The status of the report (e.g., pending, resolved).
   */
  private String status;

  /**
   * The date and time when the report was created.
   */
  private LocalDateTime date;

  /**
   * Returns the date and time the report was created.
   *
   * @return the date and time of the report
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Sets the date and time of the report.
   *
   * @param date the date and time to set for the report
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Returns the unique ID of the report.
   *
   * @return the ID of the report
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the current status of the report (e.g., pending, resolved).
   *
   * @return the status of the report
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status of the report.
   *
   * @param status the status to set for the report
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Returns the comment or description associated with the report.
   *
   * @return the comment of the report
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment for the report.
   *
   * @param comment the comment to set for the report
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Returns the ID of the admin who handled the report.
   *
   * @return the ID of the admin
   */
  public int getAdmin_id() {
    return admin_id;
  }

  /**
   * Sets the admin ID associated with the report.
   *
   * @param admin_id the admin ID to set
   */
  public void setAdmin_id(int admin_id) {
    this.admin_id = admin_id;
  }

  /**
   * Returns the ID of the user who was reported.
   *
   * @return the ID of the reported user
   */
  public int getReported_id() {
    return reported_id;
  }

  /**
   * Sets the ID of the user being reported.
   *
   * @param reported_id the reported user ID to set
   */
  public void setReported_id(int reported_id) {
    this.reported_id = reported_id;
  }

  /**
   * Returns the ID of the user who filed the report.
   *
   * @return the ID of the reporting user
   */
  public int getReporting_id() {
    return reporting_id;
  }

  /**
   * Sets the ID of the user filing the report.
   *
   * @param reporting_id the reporting user ID to set
   */
  public void setReporting_id(int reporting_id) {
    this.reporting_id = reporting_id;
  }

  /**
   * Sets the unique ID of the report.
   *
   * @param id the ID to set for the report
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns a string representation of the report, including all key fields.
   *
   * @return a string representation of the report including the reported user id,
   * reporting user id, comment, status, admin id and date.
   */
  @Override
  public String toString() {
    return "SitterReview{" +
        "id='" + id + '\'' +
        ", Reported id='" + reported_id + '\'' +
        ", Reporting id=" + reporting_id +
        ", Comment='" + comment + '\'' +
        ", Status='" + status + '\'' +
        ", Admin id='" + admin_id + '\'' +
        ", Date=" + date +
        '}';
  }
}

