using DTOs.Application;
using DTOs.HouseProfile;

namespace DTOs.HouseListing;

public class HouseListingDto
{
    public int Id { get; set; }
    public int ProfileId { get; set; }
    public DateOnly StartDate { get; set; }
    public DateOnly EndDate { get; set; }
    public string? Status { get; set; }
    
    public HouseProfileDto? Profile { get; set; }
    public List<ApplicationDto>? Applications { get; set; }
}