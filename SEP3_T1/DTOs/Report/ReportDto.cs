using DTOs.HouseOwner;
using DTOs.HouseSitter;

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
    
    public HouseOwnerDto? ReportingOwner { get; set; }
    public HouseSitterDto? ReportingSitter { get; set; }
    public HouseOwnerDto? ReportedOwner { get; set; }
    public HouseSitterDto? ReportedSitter { get; set; }
    // public AdminDto Admin { get; set; }
}