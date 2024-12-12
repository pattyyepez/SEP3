using DTOs.HouseListing;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface IHouseListingController
{
    // Get: api/HouseListing?includeProfile=true
    Task<IActionResult> GetAllHouseListings(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile);

    // GET: api/HouseListing/{id}?includeProfile=true
    Task<IActionResult> GetHouseListing(int id,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile);

    Task<IActionResult> GetListingsByProfile([FromQuery] int? profileId);


    Task<IActionResult> GetListingsByOwner(int ownerId,
        [FromServices] IHouseProfileRepository profileRepo);

    // GET api/HouseListing/GetListingsByOwnerStatus/{ownerId}/{status}?includeApplications=true&includeProfiles=true

    Task<IActionResult> GetListingsByOwnerStatus(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IApplicationRepository applicationRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeApplications,
        [FromQuery] bool includeProfiles,
        int ownerId, string status);

    Task<IActionResult> GetWaitingForReviewBySitter(
        [FromServices] IHouseReviewRepository reviewRepo,
        [FromServices] IApplicationRepository applicationRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfiles,
        int sitterId);

    // POST: api/HouseListing
    Task<IActionResult> CreateHouseListing(
        [FromBody] CreateHouseListingDto createDto);

    // PUT: api/HouseListing
    Task<IActionResult> UpdateHouseListing(
        [FromBody] UpdateHouseListingDto updateDto);

    // DELETE: api/HouseListing/{id}
    Task<IActionResult> DeleteHouseListing(int id);

    Task<IActionResult> GetFilteredListings(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] FilteredHouseListingsDto filter);
}