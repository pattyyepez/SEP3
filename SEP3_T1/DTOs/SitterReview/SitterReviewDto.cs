using DTOs.HouseOwner;
using DTOs.HouseSitter;

namespace DTOs.SitterReview;

public class SitterReviewDto
{
    public int OwnerId { get; set; }
    public int SitterId { get; set; }
    public int Rating { get; set; }
    public string? Comment { get; set; }
    public DateTime Date { get; set; }
    
    public HouseOwnerDto? Owner { get; set; }
    public HouseSitterDto? Sitter { get; set; }
    public bool? Editable { get; set; }
}