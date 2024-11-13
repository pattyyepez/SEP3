namespace DTOs.HouseOwner;

public class CreateHouseOwnerDto
{
    // attributes from User
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? Password { get; set; }
    public string? ProfilePicture { get; set; }
    public string? CPR { get; set; }
    public string? Phone { get; set; }
  
    // attributes from HouseOwner
    public string? Address { get; set; }
    public string? Biography { get; set; }
}