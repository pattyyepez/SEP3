namespace DTOs.HouseReview;

public class HouseReviewDto
{
    public int Id { get; set; }
    public int ProfileId { get; set; }
    public int SitterId { get; set; }
    public int Rating{ get; set; }
    public string? Comment { get; set; }
    public DateTime Date { get; set; }
}