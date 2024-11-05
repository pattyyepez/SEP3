using DTOs.HouseListing;

namespace RepositoryContracts;

public interface IHouseListingRepository
{
    Task<HouseListingDto> AddAsync(CreateHouseListingDto houseOwner);
    Task<HouseListingDto> UpdateAsync(int id, UpdateHouseListingDto houseOwner);
    Task DeleteAsync(int id);
    Task<HouseListingDto> GetSingleAsync(int id);
    IQueryable<HouseListingDto> GetAll();
}