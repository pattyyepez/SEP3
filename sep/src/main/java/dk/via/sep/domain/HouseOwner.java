package dk.via.sep.domain;

public class HouseOwner {

  private Long ownerId;
  private String name;
  private String address;
  private String phone;
  private String email;
  private Boolean isVerified;

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getVerified() {
    return isVerified;
  }

  public void setVerified(Boolean isVerified) {
    this.isVerified = isVerified;
  }
}
