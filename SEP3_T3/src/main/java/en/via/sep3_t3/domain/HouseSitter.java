package en.via.sep3_t3.domain;

import java.util.ArrayList;
import java.util.List;

public class HouseSitter extends User
{
  private String experience;
  private String biography;
  private List<String> pictures = new ArrayList<>();
  private List<String> skills = new ArrayList<>();

  public String getBiography()
  {
    return biography;
  }

  public String getExperience()
  {
    return experience;
  }

  public void setBiography(String biography)
  {
    this.biography = biography;
  }

  public void setExperience(String experience)
  {
    this.experience = experience;
  }

  public List<String> getPictures()
  {
    return pictures;
  }

  public void setPictures(ArrayList<String> pictures)
  {
    this.pictures = pictures;
  }

  public List<String> getSkills()
  {
    return skills;
  }

  public void setSkills(ArrayList<String> skills)
  {
    this.skills = skills;
  }

  public String toString()
  {
    return super.toString()+ " - House Sitter{"
        + "Biography='" + biography + '\''
        + ", Experience='" + experience + '\''
        + ", Skills='" + skills + '\''
        + ", Pictures='" + pictures + '\''
        +'}';
  }
}
