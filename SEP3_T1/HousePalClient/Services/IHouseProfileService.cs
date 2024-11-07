using DTOs.HouseProfile;

namespace Services;

public interface IHouseProfileService
{
    Task<HouseProfileDto> AddAsync(HouseProfileDto houseProfile);
    Task<HouseProfileDto> UpdateAsync(HouseProfileDto houseProfile);
    Task DeleteAsync(int id);
    Task<HouseProfileDto> GetSingleAsync(int id);
    IQueryable<HouseProfileDto> GetAll();
}