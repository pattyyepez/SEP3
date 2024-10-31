package en.via.sep3_t3.domain;

import java.util.ArrayList;

public class HouseSitter extends User
{
  private String experience;
  private String biography;
  private ArrayList<SitterPicture> pictures;
  private ArrayList<Skill> skills;

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

  public ArrayList<SitterPicture> getPictures()
  {
    return pictures;
  }

  public void setPictures(ArrayList<SitterPicture> pictures)
  {
    this.pictures = pictures;
  }

  public ArrayList<Skill> getSkills()
  {
    return skills;
  }

  public void setSkills(ArrayList<Skill> skills)
  {
    this.skills = skills;
  }

  public String toString()
  {
    return super.toString()+ " - House Sitter{"
        + "Biography='" + biography + '\''
        + ", Experience='" + experience + '\'' + ", "
        + ", Skills='" + skills + '\'' + ", "
        + ", Pictures='" + pictures + '\''
        +'}';
  }
}
