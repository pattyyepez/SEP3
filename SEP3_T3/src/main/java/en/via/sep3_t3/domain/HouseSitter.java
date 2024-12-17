package en.via.sep3_t3.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a house sitter in the system.
 * A house sitter is one of 2 types of user, one that is looking for a house to house-sit
 * in exchange for staying at that location for free.
 * On top of the User provided details,
 * extra information includes the sitter's relevant experience, a biography,
 * pictures of the house sitter and their skills.
 */
public class HouseSitter extends User {

  /**
   * The experience of the house sitter,
   * ideally relevant experiences making them good for house-sitting a given location.
   */
  private String experience;

  /**
   * The biography of the house sitter, providing more personal information.
   */
  private String biography;

  /**
   * A list of file names of pictures associated with the house sitter.
   */
  private List<String> pictures = new ArrayList<>();

  /**
   * A list of skills possessed by the house sitter.
   */
  private List<String> skills = new ArrayList<>();

  /**
   * Retrieves the biography of the house sitter.
   *
   * @return the biography of the house sitter.
   */
  public String getBiography() {
    return biography;
  }

  /**
   * Sets the biography of the house sitter.
   *
   * @param biography the biography to set for the house sitter.
   */
  public void setBiography(String biography) {
    this.biography = biography;
  }

  /**
   * Retrieves the experience of the house sitter.
   *
   * @return the experience of the house sitter.
   */
  public String getExperience() {
    return experience;
  }

  /**
   * Sets the experience of the house sitter.
   *
   * @param experience the experience to set for the house sitter.
   */
  public void setExperience(String experience) {
    this.experience = experience;
  }

  /**
   * Retrieves the list of pictures associated with the house sitter.
   *
   * @return a list of picture URLs or paths.
   */
  public List<String> getPictures() {
    return pictures;
  }

  /**
   * Sets the list of pictures associated with the house sitter.
   *
   * @param pictures a list of picture URLs or paths to set.
   */
  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }

  /**
   * Retrieves the list of skills possessed by the house sitter.
   *
   * @return a list of skills.
   */
  public List<String> getSkills() {
    return skills;
  }

  /**
   * Sets the list of skills possessed by the house sitter.
   *
   * @param skills a list of skills to set for the house sitter.
   */
  public void setSkills(List<String> skills) {
    this.skills = skills;
  }

  /**
   * Provides a string representation of the HouseSitter object.
   *
   * @return a string containing details of the house sitter including biography,
   * experience, skills, and pictures.
   */
  @Override
  public String toString() {
    return super.toString() + " - House Sitter{" +
        "Biography='" + biography + '\'' +
        ", Experience='" + experience + '\'' +
        ", Skills='" + skills + '\'' +
        ", Pictures='" + pictures + '\'' +
        '}';
  }
}

