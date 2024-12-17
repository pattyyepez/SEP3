package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Represents an application submitted by a sitter for a specific listing
 * in the system. It contains details about the applying sitter,
 * the listing they are applying for, the sitter's message for the owner,
 * the status of the application, and the date of creation.
 */
public class Application {

  /**
   * The unique identifier for the listing this application is for.
   */
  @Id
  private int listing_id;

  /**
   * The unique identifier for the sitter submitting the application.
   */
  @Id
  private int sitter_id;

  /**
   * The message provided by the sitter as part of their application.
   */
  private String message;

  /**
   * The current status of the application ("Pending", "Approved", "Rejected", "Confirmed", "Canceled").
   */
  private String status;

  /**
   * The date and time when the application was created.
   */
  private LocalDateTime date;

  /**
   * Retrieves the unique identifier for the listing.
   *
   * @return the listing ID associated with this application.
   */
  public int getListing_id() {
    return listing_id;
  }

  /**
   * Sets the unique identifier for the listing.
   *
   * @param listing_id the listing ID to set for this application.
   */
  public void setListing_id(int listing_id) {
    this.listing_id = listing_id;
  }

  /**
   * Retrieves the unique identifier for the sitter.
   *
   * @return the sitter ID associated with this application.
   */
  public int getSitter_id() {
    return sitter_id;
  }

  /**
   * Sets the unique identifier for the sitter.
   *
   * @param sitter_id the sitter ID to set for this application.
   */
  public void setSitter_id(int sitter_id) {
    this.sitter_id = sitter_id;
  }

  /**
   * Retrieves the message provided by the sitter.
   *
   * @return the message included in the application.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message for the application.
   *
   * @param message the message to set for this application.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Retrieves the current status of the application.
   *
   * @return the status of the application.
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status of the application.
   *
   * @param status the status to set for this application.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Retrieves the date and time when the application was submitted.
   *
   * @return the submission date and time of the application.
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Sets the submission date and time for the application.
   *
   * @param date the date and time to set for this application.
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Provides a string representation of the Application object.
   *
   * @return a string containing details of the application including
   * the listing ID, sitter ID, message, status, and date.
   */
  @Override
  public String toString() {
    return "Application{" +
        "Listing id='" + listing_id + '\'' +
        ", Sitter_id=" + sitter_id +
        ", Message='" + message + '\'' +
        ", Status='" + status + '\'' +
        ", Date=" + date +
        '}';
  }
}
