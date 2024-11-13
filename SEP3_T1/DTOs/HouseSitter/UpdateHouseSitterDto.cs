namespace DTOs.HouseSitter;

public class UpdateHouseSitterDto
{
    // attributes from User
    // public int UserId { get; set; }
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? Password { get; set; }
    public string? ProfilePicture { get; set; }
    public string? CPR { get; set; }
    public string? Phone { get; set; }
    public bool IsVerified { get; set; }
    public int AdminId { get; set; }
  
    // attributes from HouseOwner
    public string? Experience { get; set; }
    public string? Biography { get; set; }
    public List<string>? Pictures { get; set; }
    public List<string>? Skills { get; set; }
}