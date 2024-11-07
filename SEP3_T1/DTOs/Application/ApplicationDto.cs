using DTOs.HouseListing;
using DTOs.HouseSitter;

namespace DTOs.Application;

public class ApplicationDto
{
    public int ListingId { get; set; }
    public int SitterId { get; set; }
    public string? Message { get; set; }
    public string? Status { get; set; }
    public DateTime Date { get; set; }
    
    public HouseListingDto? Listing { get; set; }
    public HouseSitterDto? Sitter { get; set; }
}