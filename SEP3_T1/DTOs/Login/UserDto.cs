namespace DTOs.Login;

public class UserDto
{
    public int UserId { get; set; }
    public string? Email { get; set; }
    public string? Password { get; set; }
    public string? ProfilePicture { get; set; }
    public string? CPR { get; set; }
    public string? Phone { get; set; }
    public bool IsVerified { get; set; }
    public int AdminId { get; set; }
}