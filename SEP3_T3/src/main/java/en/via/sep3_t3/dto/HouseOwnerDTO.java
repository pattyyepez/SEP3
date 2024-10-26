package en.via.sep3_t3.dto;

public class HouseOwnerDTO
{

  private int ownerId;
  private String address;
  private String biography;

  public HouseOwnerDTO(int ownerId, String address, String biography) {
    this.ownerId = ownerId;
    this.address = address;
    this.biography = biography;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }
}
