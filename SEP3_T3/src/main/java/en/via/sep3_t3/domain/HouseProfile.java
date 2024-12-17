package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a house profile in the system.
 * A house profile contains details about a house including
 * a title, description, an owner_id identifying the owner of the house,
 * an address, city, region (location in general has been delimited to Denmark only),
 * amenities, chores, rules, and associated pictures.
 */
public class HouseProfile {

  /**
   * The unique identifier for the house profile.
   */
  @Id
  private int id;

  /**
   * The title of the house profile.
   */
  private String title;

  /**
   * A detailed description of the house.
   */
  private String description;

  /**
   * The city where the house is located.
   */
  private String city;

  /**
   * The unique identifier of the owner associated with this house profile.
   */
  private int owner_id;

  /**
   * The address of the house.
   */
  private String address;

  /**
   * The region where the house is located (e.g., Midtjylland, Sjaelland).
   */
  private String region;

  /**
   * A list of amenities available at the house.
   */
  private List<String> amenities = new ArrayList<>();

  /**
   * A list of chores expected to be performed at the house.
   */
  private List<String> chores = new ArrayList<>();

  /**
   * A list of rules for the house.
   */
  private List<String> rules = new ArrayList<>();

  /**
   * A list of file names of pictures of the house.
   */
  private List<String> pictures = new ArrayList<>();

  /**
   * Retrieves the unique identifier for the house profile.
   *
   * @return the unique ID of the house profile.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the house profile.
   *
   * @param id the unique ID to assign to the house profile.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retrieves the title of the house profile.
   *
   * @return the title of the house profile.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the house profile.
   *
   * @param title the title to set for the house profile.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the house.
   *
   * @return the description of the house.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the house.
   *
   * @param description the description to set for the house.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Retrieves the city where the house is located.
   *
   * @return the city of the house.
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the city where the house is located.
   *
   * @param city the city to set for the house.
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Retrieves the unique identifier of the house owner.
   *
   * @return the owner ID associated with this house profile.
   */
  public int getOwner_id() {
    return owner_id;
  }

  /**
   * Sets the unique identifier of the house owner.
   *
   * @param owner_id the owner ID to associate with this house profile.
   */
  public void setOwner_id(int owner_id) {
    this.owner_id = owner_id;
  }

  /**
   * Retrieves the address of the house.
   *
   * @return the address of the house.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of the house.
   *
   * @param address the address to set for the house.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Retrieves the region where the house is located.
   *
   * @return the region of the house.
   */
  public String getRegion() {
    return region;
  }

  /**
   * Sets the region where the house is located.
   *
   * @param region the region to set for the house.
   */
  public void setRegion(String region) {
    this.region = region;
  }

  /**
   * Retrieves the list of pictures of the house.
   *
   * @return a list of picture URLs or paths.
   */
  public List<String> getPictures() {
    return pictures;
  }

  /**
   * Sets the list of pictures of the house.
   *
   * @param pictures a list of picture URLs or paths to set for the house.
   */
  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }

  /**
   * Retrieves the list of rules for the house.
   *
   * @return a list of rules for the house.
   */
  public List<String> getRules() {
    return rules;
  }

  /**
   * Sets the list of rules for the house.
   *
   * @param rules a list of rules to set for the house.
   */
  public void setRules(List<String> rules) {
    this.rules = rules;
  }

  /**
   * Retrieves the list of amenities available at the house.
   *
   * @return a list of amenities for the house.
   */
  public List<String> getAmenities() {
    return amenities;
  }

  /**
   * Sets the list of amenities available at the house.
   *
   * @param amenities a list of amenities to set for the house.
   */
  public void setAmenities(List<String> amenities) {
    this.amenities = amenities;
  }

  /**
   * Retrieves the list of chores expected to be performed at the house.
   *
   * @return a list of chores for the house.
   */
  public List<String> getChores() {
    return chores;
  }

  /**
   * Sets the list of chores expected to be performed at the house.
   *
   * @param chores a list of chores to set for the house.
   */
  public void setChores(List<String> chores) {
    this.chores = chores;
  }

  /**
   * Provides a string representation of the HouseProfile object.
   *
   * @return a string containing details of the house profile including
   * the ID, title, description, city, owner ID, address, region, amenities,
   * chores, rules, and pictures.
   */
  @Override
  public String toString() {
    return "HouseProfile{" +
        "description='" + description + '\'' +
        ", city='" + city + '\'' +
        ", owner_id=" + owner_id +
        ", address='" + address + '\'' +
        ", region='" + region + '\'' +
        ", amenities=" + amenities +
        ", chores=" + chores +
        ", rules=" + rules +
        ", pictures=" + pictures +
        '}';
  }
}

