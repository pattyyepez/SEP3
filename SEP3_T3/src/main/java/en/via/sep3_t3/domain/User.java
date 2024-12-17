package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

/**
 * Represents a user in the system with various attributes such as an
 * identifying user id, a name, email, password, profile picture, CPR number,
 * phone number, an isVerified flag and the id of the admin responsible for
 * verifying this user's account.
 * This class is the superclass of specific user types (i.e., HouseOwner or HouseSitter) which extend it.
 * As such, this is an abstract class ensuring that a user that isn't of either type cannot be created.
 */
public abstract class User {

  /**
   * The unique ID of the user.
   */
  @Id
  private int userId;

  /**
   * The name of the user.
   */
  private String name;

  /**
   * The email address of the user.
   */
  private String email;

  /**
   * The password associated with the user's account.
   */
  private String password;

  /**
   * The file name of the user's profile picture.
   */
  private String profilePicture;

  /**
   * The CPR (Danish SSN) number of the user.
   */
  private String CPR;

  /**
   * The phone number of the user.
   */
  private String phone;

  /**
   * A flag indicating whether the user is verified (true if verified, false otherwise).
   */
  private boolean isVerified;

  /**
   * The ID of the admin who verified the user.
   */
  private int adminId;

  /**
   * Returns the unique ID of the user.
   *
   * @return the user ID
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Sets the unique ID for the user.
   *
   * @param userId the user ID to set
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Returns the name of the user.
   *
   * @return the name of the user
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the user.
   *
   * @param name the name to set for the user
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the email address of the user.
   *
   * @return the email of the user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of the user.
   *
   * @param email the email to set for the user
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password of the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password for the user's account.
   *
   * @param password the password to set for the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the CPR number of the user.
   *
   * @return the CPR number of the user
   */
  public String getCPR() {
    return CPR;
  }

  /**
   * Sets the CPR number for the user.
   *
   * @param CPR the CPR number to set for the user
   */
  public void setCPR(String CPR) {
    this.CPR = CPR;
  }

  /**
   * Returns the phone number of the user.
   *
   * @return the phone number of the user
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Sets the phone number for the user.
   *
   * @param phone the phone number to set for the user
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Returns the profile picture file name of the user.
   *
   * @return the profile picture of the user
   */
  public String getProfilePicture() {
    return profilePicture;
  }

  /**
   * Sets the profile picture file name for the user.
   *
   * @param profilePicture the profile picture to set for the user
   */
  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  /**
   * Returns whether the user is verified.
   *
   * @return true if the user is verified, false otherwise
   */
  public boolean isVerified() {
    return isVerified;
  }

  /**
   * Sets the verification status for the user.
   *
   * @param verified the verification status to set for the user
   */
  public void setVerified(boolean verified) {
    isVerified = verified;
  }

  /**
   * Returns the ID of the admin who verified the user.
   *
   * @return the admin ID
   */
  public int getAdminId() {
    return adminId;
  }

  /**
   * Sets the ID of the admin who verified or manages the user.
   *
   * @param adminId the admin ID to set for the user
   */
  public void setAdminId(int adminId) {
    this.adminId = adminId;
  }

  /**
   * Returns a string representation of the user, including key fields.
   *
   * @return a string representation of the user including a userId, name, email,
   * password, profilePicture, CPR number, phone number, isVerified boolean flag, and adminId
   */
  @Override
  public String toString() {
    return "User{" +
        "userId=" + userId +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", profilePicture='" + profilePicture + '\'' +
        ", CPR='" + CPR + '\'' +
        ", phone='" + phone + '\'' +
        ", isVerified=" + isVerified +
        ", adminId=" + adminId +
        '}';
  }
}