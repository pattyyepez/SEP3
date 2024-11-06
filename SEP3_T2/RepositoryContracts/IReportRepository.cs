using DTOs.Report;

namespace RepositoryContracts;

public interface IReportRepository
{
    Task<ReportDto> AddAsync(CreateReportDto houseSitter);
    Task<ReportDto> UpdateAsync(int id, UpdateReportDto houseSitter);
    Task DeleteAsync(int id);
    Task<ReportDto> GetSingleAsync(int id);
    IQueryable<ReportDto> GetAll();
}