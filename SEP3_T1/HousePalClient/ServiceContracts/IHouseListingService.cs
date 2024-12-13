using DTOs.HouseListing;

namespace HousePalClient.ServiceContracts;

public interface IHouseListingService
{
    Task<HouseListingDto> AddAsync(CreateHouseListingDto houseListing);
    Task DeleteAsync(int id);
    Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing);
    Task<HouseListingDto> GetSingleAsync(int id, bool details);
    IQueryable<HouseListingDto> GetAll();
    IQueryable<HouseListingDto> GetAllDetailedByOwner(int ownerId);
    IQueryable<HouseListingDto> GetConfirmedStaysHo(int ownerId);
    IQueryable<HouseListingDto> GetPastStaysHo(int ownerId);
    IQueryable<HouseListingDto> GetConfirmedStaysHs(int sitterId);
    IQueryable<HouseListingDto> GetPastStaysHs(int sitterId);
    IQueryable<HouseListingDto> GetBrowseListings(FilteredHouseListingsDto? filter);
    IQueryable<HouseListingDto> GetAllByProfile(int profileId);
    IQueryable<HouseListingDto> GetAllByOwner(int ownerId);
    IQueryable<HouseListingDto> GetAllByOwnerStatus(int ownerId, string status, bool includeApplications, bool includeProfiles);
    IQueryable<HouseListingDto> GetWaitingForReviewSitter(int sitterId, bool includeProfiles);
    IQueryable<HouseListingDto> GetFilteredListings(FilteredHouseListingsDto filter);
}