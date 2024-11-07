using DTOs.HouseListing;

namespace Services;

public interface IHouseListingService
{
    Task<HouseListingDto> AddAsync(HouseListingDto houseListing);
    Task<HouseListingDto> UpdateAsync(HouseListingDto houseListing);
    Task DeleteAsync(int id);
    Task<HouseListingDto> GetSingleAsync(int id);
    IQueryable<HouseListingDto> GetAll();
}