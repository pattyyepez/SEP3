using DTOs.HouseListing;

namespace HousePalClient.ServiceContracts;

public interface IHouseListingService
{
    Task<HouseListingDto> AddAsync(CreateHouseListingDto houseListing);
    Task DeleteAsync(int id);
    Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing);
    Task<HouseListingDto> GetSingleAsync(int id, bool details);
    IQueryable<HouseListingDto> GetAll();
    IQueryable<HouseListingDto> GetAllByProfile(int profileId);
    IQueryable<HouseListingDto> GetAllByOwner(int ownerId);
    IQueryable<HouseListingDto> GetAllByOwnerStatus(int ownerId, string status, bool includeApplications, bool includeProfiles);
    IQueryable<HouseListingDto> GetWaitingForReviewSitter(int sitterId, bool includeProfiles);
    IQueryable<HouseListingDto> GetFilteredListings(FilteredHouseListingsDto filter);
}