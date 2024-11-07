using DTOs.Application;

namespace Services;

public interface IApplicationService
{
    Task<ApplicationDto> AddAsync(ApplicationDto application);
    Task<ApplicationDto> UpdateAsync(ApplicationDto application);
    Task DeleteAsync(int id);
    Task<ApplicationDto> GetSingleAsync(int id);
    IQueryable<ApplicationDto> GetAll();
}