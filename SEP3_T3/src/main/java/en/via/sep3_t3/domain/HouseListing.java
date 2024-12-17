package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Represents a house listing in the system.
 * A house listing includes information about the house profile being listed,
 * availability dates, and the current status of the listing.
 */
public class HouseListing {

  /**
   * The unique identifier for the house listing.
   */
  @Id
  private int id;

  /**
   * The unique identifier for the house profile associated with this listing.
   */
  private int profile_id;

  /**
   * The start date of this house listing.
   */
  private Date startDate;

  /**
   * The end date of this house listing.
   */
  private Date endDate;

  /**
   * The current status of the house listing ("Open", "Closed").
   */
  private String status;

  /**
   * Retrieves the unique identifier for the house profile.
   *
   * @return the profile ID associated with this house listing.
   */
  public int getProfile_id() {
    return profile_id;
  }

  /**
   * Sets the unique identifier for the house profile.
   *
   * @param profile_id the profile ID to associate with this house listing.
   */
  public void setProfile_id(int profile_id) {
    this.profile_id = profile_id;
  }

  /**
   * Retrieves the unique identifier for the house listing.
   *
   * @return the unique ID of this house listing.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the house listing.
   *
   * @param id the unique ID to assign to this house listing.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retrieves the start date of the availability period.
   *
   * @return the start date of this house listing.
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Sets the start date of the availability period.
   *
   * @param startDate the start date to set for this house listing.
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * Retrieves the end date of the availability period.
   *
   * @return the end date of this house listing.
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * Sets the end date of the availability period.
   *
   * @param endDate the end date to set for this house listing.
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * Retrieves the current status of the house listing.
   *
   * @return the status of the house listing.
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the current status of the house listing.
   *
   * @param status the status to set for this house listing.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Provides a string representation of the HouseListing object.
   *
   * @return a string containing details of the house listing including
   * the ID, profile ID, start date, end date, and status.
   */
  @Override
  public String toString() {
    return "HouseListing{" +
        "HouseListing id='" + id + '\'' +
        "HouseProfile id='" + profile_id + '\'' +
        ", Start Date='" + startDate + '\'' +
        ", End Date=" + endDate +
        ", Status='" + status + '\'' +
        '}';
  }
}
