namespace DTOs.HouseListing;

public class CreateHouseListingDto
{
    public int ProfileId { get; set; }
    public DateTime StartDate { get; set; }
    public DateTime EndDate { get; set; }
    public string? Status { get; set; }
}