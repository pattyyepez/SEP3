package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

public class Report
{
  @Id private int id;
  private int reporting_id;
  private int reported_id;
  private int admin_id;
  private String comment;
  private String status;
  private LocalDateTime date;

  public LocalDateTime getDate()
  {
    return date;
  }

  public void setDate(LocalDateTime date)
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

  public int getReported_id()
  {
    return reported_id;
  }

  public void setReported_id(int reported_id)
  {
    this.reported_id = reported_id;
  }

  public int getReporting_id()
  {
    return reporting_id;
  }

  public void setReporting_id(int reporting_id)
  {
    this.reporting_id = reporting_id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String toString() {
    return "SitterReview{" +
        "id='" + id + '\'' +
        ", HouseSitter id='" + reported_id + '\'' +
        ", HouseOwner id=" + reporting_id +
        ", Comment='" + comment + '\'' +
        ", Status='" + status + '\'' +
        ", Admin id='" + admin_id + '\'' +
        ", Date=" + date +
        '}';
  }
}
