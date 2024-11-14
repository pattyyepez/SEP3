using DTOs.HouseProfile;

namespace RepositoryContracts;

public interface IHouseProfileRepository
{
    Task<HouseProfileDto> AddAsync(CreateHouseProfileDto houseProfile);
    Task<HouseProfileDto> UpdateAsync(int id, UpdateHouseProfileDto houseProfile);
    Task DeleteAsync(int id);
    Task<HouseProfileDto> GetSingleAsync(int id);
    IQueryable<HouseProfileDto> GetAll();
    IQueryable<String> GetAllChores();
    IQueryable<String> GetAllRules();
    IQueryable<String> GetAllAmenities();
}