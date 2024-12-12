using DTOs.Application;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface IApplicationController
{
        // GET: api/Application?includeListing=true&includeSitter=true
        Task<IActionResult> GetAllApplications(
            [FromServices] IHouseListingRepository listingRepo,
            [FromServices] IHouseSitterRepository sitterRepo,
            [FromQuery] bool includeListing,
            [FromQuery] bool includeSitter);

    // GET: api/Application/{id}?includeListing=true&includeSitter=true
    Task<IActionResult> GetApplication(int listingId, int sitterId,
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListing,
        [FromQuery] bool includeSitter);
    
    // GET: https://localhost:7134/api/Application/GetApplication/{listingId}
        Task<IActionResult> GetApplicationByListing(
        int listingId, string status,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeSitter);
    
// GET https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}
        Task<IActionResult> GetApplicationsByUser(
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListings,
        [FromQuery] bool includeProfiles,
        int? userId, string? status);

    // POST: api/Application
        Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto);

    // PUT: api/Application/{id}
        Task<IActionResult> UpdateApplication(int listingId,
        int sitterId,
        [FromBody] UpdateApplicationDto updateDto);

    // DELETE: api/Application/{id}
        Task<IActionResult> DeleteApplication(int listingId,
        int sitterId);
}