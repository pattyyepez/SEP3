package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class HouseReview
{
  @Id private int id;
  private int profile_id;
  private int sitter_id;
  private int rating;
  private String comment;
  private Date date;

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

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

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
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
        "id='" + id + '\'' +
        ", HouseSitter id='" + sitter_id + '\'' +
        ", HouseProfile id=" + profile_id +
        ", Comment='" + comment + '\'' +
        ", Rating='" + rating + '\'' +
        ", Date=" + date +
        '}';
  }
}
