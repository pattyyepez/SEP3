package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

/**
 * Represents a review left by a house owner about a house sitter that has stayed at their house,
 * containing details such as owner_id identifying the house owner leaving the review,
 * sitter_id identifying the house sitter being reviewed, a rating, comment, and the date of the review.
 */
public class SitterReview {

  /**
   * The ID of the house owner who left the review.
   */
  @Id
  private int owner_id;

  /**
   * The ID of the house sitter being reviewed.
   */
  @Id
  private int sitter_id;

  /**
   * The rating given by the house owner to the house sitter (integer 1-5 inclusive).
   */
  private int rating;

  /**
   * The comment left by the house owner in the review.
   */
  private String comment;

  /**
   * The date and time when the review was created.
   */
  private LocalDateTime date;

  /**
   * Returns the ID of the house owner who left the review.
   *
   * @return the ID of the house owner
   */
  public int getOwner_id() {
    return owner_id;
  }

  /**
   * Sets the ID of the house owner who left the review.
   *
   * @param owner_id the ID to set for the house owner
   */
  public void setOwner_id(int owner_id) {
    this.owner_id = owner_id;
  }

  /**
   * Returns the ID of the house sitter being reviewed.
   *
   * @return the ID of the house sitter
   */
  public int getSitter_id() {
    return sitter_id;
  }

  /**
   * Sets the ID of the house sitter being reviewed.
   *
   * @param sitter_id the ID to set for the house sitter
   */
  public void setSitter_id(int sitter_id) {
    this.sitter_id = sitter_id;
  }

  /**
   * Returns the rating given by the house owner.
   *
   * @return the rating of the house sitter
   */
  public int getRating() {
    return rating;
  }

  /**
   * Sets the rating for the house sitter.
   *
   * @param rating the rating to set for the review
   */
  public void setRating(int rating) {
    this.rating = rating;
  }

  /**
   * Returns the comment left by the house owner in the review.
   *
   * @return the comment of the review
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment for the review.
   *
   * @param comment the comment to set for the review
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Returns the date and time when the review was created.
   *
   * @return the date and time of the review
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Sets the date and time when the review was created.
   *
   * @param date the date and time to set for the review
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Returns a string representation of the review, including all key fields.
   *
   * @return a string representation of the review including a house sitter id,
   * a house owner id, comment, rating and date.
   */
  @Override
  public String toString() {
    return "SitterReview{" +
        " HouseSitter id='" + sitter_id + '\'' +
        ", HouseOwner id=" + owner_id +
        ", Comment='" + comment + '\'' +
        ", Rating='" + rating + '\'' +
        ", Date=" + date +
        '}';
  }
}
