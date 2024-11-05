using DTOs.Application;

namespace RepositoryContracts;

public interface IApplicationRepository
{
    Task<ApplicationDto> AddAsync(CreateApplicationDto houseOwner);
    Task<ApplicationDto> UpdateAsync(int listingId, int sitterId, UpdateApplicationDto houseOwner);
    Task DeleteAsync(int listingId, int sitterId);
    Task<ApplicationDto> GetSingleAsync(int listingId, int sitterId);
    IQueryable<ApplicationDto> GetAll();
}