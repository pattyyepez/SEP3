namespace DTOs.SitterReview;

public class SitterReviewDto
{
    public int Id { get; set; }
    public int OwnerId { get; set; }
    public int SitterId { get; set; }
    public int Rating { get; set; }
    public string? Comment { get; set; }
    public DateTime Date { get; set; }
}