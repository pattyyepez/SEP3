namespace DTOs.HouseListing;

public class CreateHouseListingDto
{
    public int ProfileId { get; set; }
    public DateOnly StartDate { get; set; }
    public DateOnly EndDate { get; set; }
}