package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

public class HouseReview
{
  @Id private int profile_id;
  @Id private int sitter_id;
  private int rating;
  private String comment;
  private LocalDateTime date;

  public int getProfile_id()
  {
    return profile_id;
  }

  public void setProfile_id(int profile_id)
  {
    this.profile_id = profile_id;
  }

  public int getSitter_id()
  {
    return sitter_id;
  }

  public void setSitter_id(int sitter_id)
  {
    this.sitter_id = sitter_id;
  }

  public int getRating()
  {
    return rating;
  }

  public String getComment()
  {
    return comment;
  }

  public LocalDateTime getDate()
  {
    return date;
  }

  public void setDate(LocalDateTime date)
  {
    this.date = date;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public void setRating(int rating)
  {
    this.rating = rating;
  }
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
