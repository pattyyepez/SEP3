package en.via.sep3_t3.domain;

public class HouseSitter extends User
{
  private String experience;
  private String biography;

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
  public String toString()
  {
    return super.toString()+ " - House Sitter{" + "Expierience='" + experience + '\'' + ", biography='"
        + biography + '\'' + '}';
  }
}
