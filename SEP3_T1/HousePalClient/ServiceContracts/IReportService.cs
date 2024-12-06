using DTOs.Report;

namespace HousePalClient.ServiceContracts;

public interface IReportService
{
    Task<ReportDto> AddAsync(ReportDto report);
    Task<ReportDto> UpdateAsync(ReportDto report);
    Task DeleteAsync(int id);
    Task<ReportDto> GetSingleAsync(int id);
    IQueryable<ReportDto> GetAll();
}