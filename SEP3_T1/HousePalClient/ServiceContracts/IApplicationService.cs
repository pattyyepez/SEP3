using DTOs.Application;

namespace Services;

public interface IApplicationService
{
    Task<ApplicationDto> AddAsync(CreateApplicationDto application);
    Task<ApplicationDto> UpdateAsync(UpdateApplicationDto application);
    Task DeleteAsync(int id);
    Task<ApplicationDto> GetSingleAsync(int id);
    IQueryable<ApplicationDto> GetAll();
}