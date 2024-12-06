using DTOs.HouseProfile;

namespace HousePalClient.ServiceContracts;

public interface IHouseProfileService
{
    Task<HouseProfileDto> AddAsync(CreateHouseProfileDto houseProfile);
    Task<HouseProfileDto> UpdateAsync(UpdateHouseProfileDto houseProfile);
    Task DeleteAsync(int id);
    Task<HouseProfileDto> GetSingleAsync(int id, bool details);
    IQueryable<HouseProfileDto> GetAll();
    IQueryable<HouseProfileDto> GetAllByOwner(int ownerId);
    IQueryable<string> GetAllChores();
    IQueryable<string> GetAllRules();
    IQueryable<string> GetAllAmenities();
}