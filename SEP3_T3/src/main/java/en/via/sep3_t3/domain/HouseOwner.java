package en.via.sep3_t3.domain;

/**
 * Represents a house owner in the system.
 * A house owner is one of 2 types of user, one that owns a house
 * that is in need of house-sitting. On top of the User provided details,
 * extra information includes the owner's address and a biography.
 */
public class HouseOwner extends User {

  /**
   * The address of the house owner.
   */
  private String address;

  /**
   * A short biography or description of the house owner.
   */
  private String biography;

  /**
   * Retrieves the address of the house owner.
   *
   * @return the address of the house owner.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of the house owner.
   *
   * @param address the address to set for the house owner.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Retrieves the biography of the house owner.
   *
   * @return the biography of the house owner.
   */
  public String getBiography() {
    return biography;
  }

  /**
   * Sets the biography of the house owner.
   *
   * @param biography the biography to set for the house owner.
   */
  public void setBiography(String biography) {
    this.biography = biography;
  }

  /**
   * Provides a string representation of the HouseOwner object.
   *
   * @return a string containing the address and biography of the house owner,
   * appended to the string representation of the User class.
   */
  @Override
  public String toString() {
    return super.toString() + " - HouseOwner{" +
        "address='" + address + '\'' +
        ", biography='" + biography + '\'' +
        '}';
  }
}
