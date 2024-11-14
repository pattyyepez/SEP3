namespace DTOs.Login;

public class UserDto
{
    public int UserId { get; set; }
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? ProfilePicture { get; set; }
    public string? CPR { get; set; }
    public string? Phone { get; set; }
    public bool IsVerified { get; set; }
    
    public string? Address { get; set; }
    public string? Biography { get; set; }
    public string? Experience { get; set; }
    public List<string>? Pictures { get; set; }
    public List<string>? Skills { get; set; }
}