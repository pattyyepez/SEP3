package en.via.sep3_t3.domain;

public class Admin
{
  private int id;
  private String email;
  private String password;

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String toString() {
    return "Admin{" +
        "id='" + id + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
