package en.via.sep3_t3.domain;

import java.util.ArrayList;

public class HouseProfile
{
  private String description;
  private String city;
  private int owner_id;
  private String address;
  private String region;
  private ArrayList<Amenity> amenities;
  private ArrayList<Chore> chores;
  private ArrayList<Rule> rules;
  private ArrayList<HousePicture> pictures;

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

  public ArrayList<Amenity> getAmenities()
  {
    return amenities;
  }

  public void setAmenities(ArrayList<Amenity> amenities)
  {
    this.amenities = amenities;
  }

  public ArrayList<Chore> getChores()
  {
    return chores;
  }

  public void setChores(ArrayList<Chore> chores)
  {
    this.chores = chores;
  }

  public ArrayList<HousePicture> getPictures()
  {
    return pictures;
  }

  public void setPictures(ArrayList<HousePicture> pictures)
  {
    this.pictures = pictures;
  }

  public ArrayList<Rule> getRules()
  {
    return rules;
  }

  public void setRules(ArrayList<Rule> rules)
  {
    this.rules = rules;
  }
}
