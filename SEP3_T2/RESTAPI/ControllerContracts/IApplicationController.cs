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
        
        Task<IActionResult> GetMyApplicationsSitter(
            [FromServices] IHouseListingRepository listingRepo,
            [FromServices] IHouseProfileRepository profileRepo,
            [FromServices] IHouseReviewRepository reviewRepo,
            [FromRoute] int sitterId);

    // GET: api/Application/{id}?includeListing=true&includeSitter=true
    Task<IActionResult> GetApplication(int listingId, int sitterId,
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListing,
        [FromQuery] bool includeSitter);

    // POST: api/Application
        Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto);

    // PUT: api/Application/{id}
        Task<IActionResult> UpdateApplication(
        [FromBody] UpdateApplicationDto updateDto);

    // DELETE: api/Application/{id}
        Task<IActionResult> DeleteApplication(int listingId,
        int sitterId);
}