namespace DTOs.Application;

public class UpdateApplicationDto
{
    public int ListingId { get; set; }
    public int SitterId { get; set; }
    public string? Status { get; set; }
}