using DTOs.HouseProfile;
using DTOs.HouseSitter;

namespace DTOs.HouseReview;

public class HouseReviewDto
{
    public int ProfileId { get; set; }
    public int SitterId { get; set; }
    public int Rating{ get; set; }
    public string? Comment { get; set; }
    public DateTime Date { get; set; }
    
    public HouseProfileDto? Profile { get; set; }
    public HouseSitterDto? Sitter { get; set; }
    public bool? Editable { get; set; }
}