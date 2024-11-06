namespace DTOs.Report;

public class ReportDto
{
    public int Id { get; set; }
    public int ReportingId { get; set; }
    public int ReportedId { get; set; }
    public int AdminId { get; set; }
    public string? Comment { get; set; }
    public string? Status { get; set; }
    public DateTime Date { get; set; }
}