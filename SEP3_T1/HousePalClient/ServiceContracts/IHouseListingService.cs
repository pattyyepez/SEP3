using DTOs.HouseListing;

namespace Services;

public interface IHouseListingService
{
    Task<HouseListingDto> AddAsync(CreateHouseListingDto houseListing);
    Task DeleteAsync(int id);
    Task<HouseListingDto> GetSingleAsync(int id, bool details);
    IQueryable<HouseListingDto> GetAll();
    IQueryable<HouseListingDto> GetAllByProfile(int profileId);
    IQueryable<HouseListingDto> GetAllByOwner(int ownerId);
    IQueryable<HouseListingDto> GetFilteredListings(FilteredHouseListingsDto filter);
}