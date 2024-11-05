namespace DTOs.Application;

public class CreateApplicationDto
{
    public int ListingId { get; set; }
    public int SitterId { get; set; }
    public string? Message { get; set; }
    public string? Status { get; set; }
}