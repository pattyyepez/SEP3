package en.via.sep3_t3.domain;

/**
 * Used to represent an administrator within the system.
 * The Admin class manages the administrator's unique ID, email, and password.
 */
public class Admin {

  /**
   * The unique identifier for the administrator.
   */
  private int id;

  /**
   * The email address of the administrator.
   */
  private String email;

  /**
   * The password for the administrator's account.
   */
  private String password;

  /**
   * Retrieves the administrator's unique ID.
   *
   * @return the unique identifier of the administrator.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the administrator's unique ID.
   *
   * @param id the unique identifier to assign to the administrator.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retrieves the email address of the administrator.
   *
   * @return the email address of the administrator.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of the administrator.
   *
   * @param email the email address to assign to the administrator.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Retrieves the password of the administrator's account.
   *
   * @return the password of the administrator.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password for the administrator's account.
   *
   * @param password the password to assign to the administrator.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Provides a string representation of the Admin object.
   *
   * @return a string containing the administrator's ID and email.
   */
  @Override
  public String toString() {
    return "Admin{" +
        "id='" + id + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}

