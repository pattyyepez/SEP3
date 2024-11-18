using DTOs.HouseListing;

namespace Services;

public interface IHouseListingService
{
    Task<HouseListingDto> AddAsync(CreateHouseListingDto houseListing);
    Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing);
    Task DeleteAsync(int id);
    Task<HouseListingDto> GetSingleAsync(int id);
    IQueryable<HouseListingDto> GetAll();
    IQueryable<HouseListingDto> GetAllByProfile(int profileId);
    IQueryable<HouseListingDto> GetAllByOwner(int ownerId);
}