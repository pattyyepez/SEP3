package en.via.sep3_t3.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class HouseProfile
{
  @Id private int id;
  private String title;
  private String description;
  private String city;
  private int owner_id;
  private String address;
  private String region;
  private List<String> amenities = new ArrayList<>();
  private List<String> chores = new ArrayList<>();
  private List<String> rules = new ArrayList<>();
  private List<String> pictures = new ArrayList<>();


  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public int getOwner_id()
  {
    return owner_id;
  }

  public void setOwner_id(int owner_id)
  {
    this.owner_id = owner_id;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getRegion()
  {
    return region;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }

  public List<String> getPictures()
  {
    return pictures;
  }

  public void setPictures(List<String> pictures)
  {
    this.pictures = pictures;
  }

  public List<String> getRules()
  {
    return rules;
  }

  public void setRules(List<String> rules)
  {
    this.rules = rules;
  }

  public List<String> getAmenities()
  {
    return amenities;
  }

  public List<String> getChores()
  {
    return chores;
  }

  public void setChores(List<String> chores)
  {
    this.chores = chores;
  }

  public void setAmenities(List<String> amenities)
  {
    this.amenities = amenities;
  }

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
