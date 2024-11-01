namespace DTOs.HouseSitter;

public class CreateHouseSitterDTO
{
    // attributes from User
    public string? Email { get; set; }
    public string? Password { get; set; }
    public string? ProfilePicture { get; set; }
    public string? CPR { get; set; }
    public string? Phone { get; set; }
  
    // attributes from HouseOwner
    public string? Experience { get; set; }
    public string? Biography { get; set; }
    public List<string>? Pictures { get; set; }
    public List<string>? Skills { get; set; }
}