package en.via.sep3_t3.domain;

public class HouseOwner extends User
{

  private String address;
  private String biography;

//  public HouseOwner(int userId, String email, String password, String profilePicture, String CPR,
//      String phone, boolean isVerified, int adminId, String address, String biography)
//  {
//    super(userId, email, password, profilePicture, CPR, phone, isVerified, adminId);
//    this.address = address;
//    this.biography = biography;
//  }

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

  @Override public String toString()
  {
    return super.toString() + " - HouseOwner{" + "address='" + address + '\'' + ", biography='"
        + biography + '\'' + '}';
  }
}