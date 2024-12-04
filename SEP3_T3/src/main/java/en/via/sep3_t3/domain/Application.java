package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Application
{
  @Id private int listing_id;
  @Id private int sitter_id;
  private String message;
  private String status;
  private LocalDateTime date;


  public int getListing_id()
  {
    return listing_id;
  }

  public void setListing_id(int listing_id)
  {
    this.listing_id = listing_id;
  }

  public int getSitter_id()
  {
    return sitter_id;
  }

  public void setSitter_id(int sitter_id)
  {
    this.sitter_id = sitter_id;
  }

  public String getMessage()
  {
    return message;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public LocalDateTime getDate()
  {
    return date;
  }

  public void setDate(LocalDateTime date)
  {
    this.date = date;
  }
  public String toString()
  { return
    "Application{" +
        "Listing id='" + listing_id + '\'' +
        ", Sitter_id=" + sitter_id +
        ", Message='" + message + '\'' +
        ", Status='" + status + '\'' +
        ", Date=" + date +
        '}';
  }
}
