package en.via.sep3_t3.domain;

public class HouseOwner
{
  private int ownerId;
  private String address;
  private String biography;

  public int getOwnerId()
  {
    return ownerId;
  }

  public void setOwnerId(int ownerId)
  {
    this.ownerId = ownerId;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getBiography()
  {
    return biography;
  }

  public void setBiography(String biography)
  {
    this.biography = biography;
  }
}