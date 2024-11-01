package en.via.sep3_t3.domain;

import java.util.Date;

public class HouseListing
{
  private int id;
  private int profile_id;
  private Date startDate;
  private Date endDate;
  private String status;

  public int getProfile_id()
  {
    return profile_id;
  }

  public void setProfile_id(int profile_id)
  {
    this.profile_id = profile_id;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }
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
