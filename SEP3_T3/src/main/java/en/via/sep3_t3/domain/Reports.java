package en.via.sep3_t3.domain;

import java.util.Date;

public class Reports
{
  private int id;
  private int owner_id;
  private int sitter_id;
  private int admin_id;
  private String comment;
  private String status;
  private Date date;

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public int getId()
  {
    return id;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public int getAdmin_id()
  {
    return admin_id;
  }

  public void setAdmin_id(int admin_id)
  {
    this.admin_id = admin_id;
  }

  public int getSitter_id()
  {
    return sitter_id;
  }

  public void setSitter_id(int sitter_id)
  {
    this.sitter_id = sitter_id;
  }

  public int getOwner_id()
  {
    return owner_id;
  }

  public void setOwner_id(int owner_id)
  {
    this.owner_id = owner_id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String toString() {
    return "SitterReview{" +
        "id='" + id + '\'' +
        ", HouseSitter id='" + sitter_id + '\'' +
        ", HouseOwner id=" + owner_id +
        ", Comment='" + comment + '\'' +
        ", Status='" + status + '\'' +
        ", Admin id='" + admin_id + '\'' +
        ", Date=" + date +
        '}';
  }
}
