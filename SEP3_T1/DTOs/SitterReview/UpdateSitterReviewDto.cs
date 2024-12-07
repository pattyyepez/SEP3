namespace DTOs.SitterReview;

public class UpdateSitterReviewDto
{
    public int OwnerId { get; set; }
    public int SitterId { get; set; }
    public int Rating { get; set; }
    public string? Comment { get; set; }
}