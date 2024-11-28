namespace DTOs.HouseListing;

public class FilteredHouseListingsDto
{
    public string? Region { get; set; }
    public string? City { get; set; }
    public int? Rating { get; set; }

    public int? StartDay { get; set; }
    public int? StartMonth { get; set; }
    public int? StartYear { get; set; }
    public int? EndDay { get; set; }
    public int? EndMonth { get; set; }
    public int? EndYear { get; set; }
    public List<string> Amenities { get; set; } = [];
    public List<string> Chores { get; set; } = [];
}