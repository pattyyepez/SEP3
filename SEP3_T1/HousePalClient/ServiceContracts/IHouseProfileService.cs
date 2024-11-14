using DTOs.HouseProfile;

namespace Services;

public interface IHouseProfileService
{
    Task<HouseProfileDto> AddAsync(CreateHouseProfileDto houseProfile);
    Task<HouseProfileDto> UpdateAsync(HouseProfileDto houseProfile);
    Task DeleteAsync(int id);
    Task<HouseProfileDto> GetSingleAsync(int id);
    IQueryable<HouseProfileDto> GetAll();
    IQueryable<HouseProfileDto> GetAllByOwner(int ownerId);
    IQueryable<string> GetAllChores();
    IQueryable<string> GetAllRules();
    IQueryable<string> GetAllAmenities();
}