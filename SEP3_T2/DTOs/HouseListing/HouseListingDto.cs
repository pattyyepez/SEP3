namespace DTOs.HouseListing;

public class HouseListingDto
{
    public int Id { get; set; }
    public int ProfileId { get; set; }
    public DateTime StartDate { get; set; }
    public DateTime EndDate { get; set; }
    public string? Status { get; set; }
}