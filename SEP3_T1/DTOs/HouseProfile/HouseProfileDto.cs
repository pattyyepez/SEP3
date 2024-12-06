using DTOs.HouseOwner;
using DTOs.HouseReview;

namespace DTOs.HouseProfile;

public class HouseProfileDto
{
    public int Id { get; set; }
    public string? Title { get; set; }
    public string? Description { get; set; }
    public string? City { get; set; }
    public int OwnerId { get; set; }
    public string? Address { get; set; }
    public string? Region { get; set; }
    public List<string>? Amenities { get; set; }
    public List<string>? Chores { get; set; }
    public List<string>? Rules { get; set; }
    public List<string>? Pictures { get; set; }
    
    public HouseOwnerDto? Owner { get; set; }
    public List<HouseReviewDto>? Reviews { get; set; }
}