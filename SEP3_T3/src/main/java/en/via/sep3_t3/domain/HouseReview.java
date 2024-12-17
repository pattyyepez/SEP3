package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

/**
 * Represents a review of a house in the system
 * created by a house sitter that has stayed at that location before.
 * A HouseReview includes a profile_id identifying the house profile being reviewed,
 * a sitter_id identifying the house sitter reviewing the house profile, a rating, comment,
 * and the date of creation of the review.
 */
public class HouseReview {

  /**
   * The unique identifier of the house profile being reviewed.
   */
  @Id
  private int profile_id;

  /**
   * The unique identifier of the sitter submitting the review.
   */
  @Id
  private int sitter_id;

  /**
   * The rating provided by the sitter (integers 1-5 inclusive).
   */
  private int rating;

  /**
   * The comment provided by the sitter about their experience.
   */
  private String comment;

  /**
   * The date and time the review was created.
   */
  private LocalDateTime date;

  /**
   * Retrieves the unique identifier of the house profile being reviewed.
   *
   * @return the house profile ID.
   */
  public int getProfile_id() {
    return profile_id;
  }

  /**
   * Sets the unique identifier of the house profile being reviewed.
   *
   * @param profile_id the house profile ID to set.
   */
  public void setProfile_id(int profile_id) {
    this.profile_id = profile_id;
  }

  /**
   * Retrieves the unique identifier of the sitter submitting the review.
   *
   * @return the sitter ID.
   */
  public int getSitter_id() {
    return sitter_id;
  }

  /**
   * Sets the unique identifier of the sitter submitting the review.
   *
   * @param sitter_id the sitter ID to set.
   */
  public void setSitter_id(int sitter_id) {
    this.sitter_id = sitter_id;
  }

  /**
   * Retrieves the rating provided by the sitter.
   *
   * @return the numeric rating.
   */
  public int getRating() {
    return rating;
  }

  /**
   * Sets the rating provided by the sitter.
   *
   * @param rating the numeric rating to set.
   */
  public void setRating(int rating) {
    this.rating = rating;
  }

  /**
   * Retrieves the comment provided by the sitter.
   *
   * @return the sitter's comment.
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment provided by the sitter.
   *
   * @param comment the comment to set.
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Retrieves the date and time the review was submitted.
   *
   * @return the review date and time.
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Sets the date and time the review was submitted.
   *
   * @param date the date and time to set.
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Provides a string representation of the HouseReview object.
   *
   * @return a string containing details of the review including the sitter ID,
   * house profile ID, comment, rating, and date.
   */
  @Override
  public String toString() {
    return "HouseReview{" +
        "HouseSitter id (ppk)='" + sitter_id + '\'' +
        ", HouseProfile id (ppk)=" + profile_id +
        ", Comment='" + comment + '\'' +
        ", Rating='" + rating + '\'' +
        ", Date=" + date +
        '}';
  }
}
