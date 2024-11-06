namespace DTOs.HouseReview;

public class CreateHouseReviewDto
{
    public int ProfileId { get; set; }
    public int SitterId { get; set; }
    public int Rating{ get; set; }
    public string? Comment { get; set; }
}