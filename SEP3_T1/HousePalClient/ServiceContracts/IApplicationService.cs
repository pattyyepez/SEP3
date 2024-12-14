using DTOs.Application;

namespace HousePalClient.ServiceContracts;

public interface IApplicationService
{
    Task<ApplicationDto> AddAsync(CreateApplicationDto application);
    Task<ApplicationDto> UpdateAsync(UpdateApplicationDto application);
    Task DeleteAsync(int listingId, int sitterId);
    Task<ApplicationDto> GetSingleAsync(int listingId, int sitterId);
    IQueryable<ApplicationDto> GetAll();
    IQueryable<ApplicationDto> GetMyApplicationsSitter(int sitterId);
}