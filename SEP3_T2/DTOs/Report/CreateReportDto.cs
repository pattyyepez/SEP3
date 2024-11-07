namespace DTOs.Report;

public class CreateReportDto
{
    public int ReportingId { get; set; }
    public int ReportedId { get; set; }
    public int AdminId { get; set; }
    public string? Comment { get; set; }
}