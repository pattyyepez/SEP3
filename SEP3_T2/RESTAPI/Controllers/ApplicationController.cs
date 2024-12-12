using DTOs.Application;
using DTOs.HouseListing;
using DTOs.HouseProfile;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class ApplicationController : ControllerBase, IApplicationController
{
    private readonly IApplicationRepository _repo;

    public ApplicationController(IApplicationRepository repo)
    {
        _repo = repo;
    }

    // GET: api/Application?includeListing=true&includeSitter=true
    [HttpGet]
    public async Task<IActionResult> GetAllApplications(
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListing,
        [FromQuery] bool includeSitter)
    {
        var response = _repo.GetAll();
        if (!includeSitter && !includeListing) return Ok(response);

        var toReturn = new List<ApplicationDto>();
        foreach (var application in response)
        {
            if (includeListing)
                application.Listing =
                    await listingRepo.GetSingleAsync(application.ListingId);

            if (includeSitter)
                application.Sitter =
                    await sitterRepo.GetSingleAsync(application.SitterId);

            toReturn.Add(application);
        }

        return Ok(toReturn.AsQueryable());
    }

    // GET: api/Application/{id}?includeListing=true&includeSitter=true
    [HttpGet("{listingId}/{sitterId}")]
    public async Task<IActionResult> GetApplication(int listingId, int sitterId,
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListing,
        [FromQuery] bool includeSitter)
    {
        var response = await _repo.GetSingleAsync(listingId, sitterId);

        if (includeListing)
            response.Listing = await listingRepo.GetSingleAsync(listingId);

        if (includeSitter)
            response.Sitter = await sitterRepo.GetSingleAsync(sitterId);

        return Ok(response);
    }

    // GET: https://localhost:7134/api/Application/GetApplication/{listingId}
    [HttpGet("{listingId:int}/{status}")]
    public async Task<IActionResult> GetApplicationByListing(
        int listingId, string status,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeSitter)
    {
        var response = _repo.GetAll().Where(a => a.ListingId == listingId);

        if (!string.IsNullOrWhiteSpace(status))
            response = response.Where(a => a.Status == status);

        foreach (var application in response)
        {
            if (includeSitter)
                application.Sitter =
                    await sitterRepo.GetSingleAsync(application.SitterId);
        }

        return Ok(response);
    }

// GET https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}
    [HttpGet("{userId}/{status}")]
    public async Task<IActionResult> GetApplicationsByUser(
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListings,
        [FromQuery] bool includeProfiles,
        int? userId, string? status)
    {
        IQueryable<ApplicationDto> applications = _repo.GetAll();

        if (!string.IsNullOrWhiteSpace(status))
        {
            applications = applications.Where(a => a.Status == status);
        }

        switch (userId)
        {
            case null:
                return NotFound();
            case 0:
                return Ok(applications);
            default:
                if (sitterRepo.GetAll().Any(s => s.UserId == userId.Value))
                {
                    applications =
                        applications.Where(a => a.SitterId == userId.Value);

                    foreach (var application in applications)
                    {
                        if (includeListings)
                        {
                            var listing = await listingRepo.GetSingleAsync(
                                application.ListingId);
                            application.Listing = new HouseListingDto
                            {
                                StartDate = listing.StartDate,
                                EndDate = listing.EndDate,
                            };
                        }

                        if (includeProfiles)
                        {
                            var profile = await profileRepo.GetSingleAsync(
                                (await listingRepo.GetSingleAsync(
                                    application.ListingId)).ProfileId);
                            application.Listing ??= new HouseListingDto();
                            application.Listing.Profile =
                                new HouseProfileDto
                                {
                                    Id = profile.Id,
                                    Title = profile.Title,
                                    Address = profile.Address,
                                    Pictures = profile.Pictures,
                                    OwnerId = profile.OwnerId,
                                    City = profile.City,
                                    Region = profile.Region
                                };
                        }
                    }

                    return Ok(applications);
                }

                var listings = listingRepo.GetAll();

                listings = listings.Except(listings
                    .ExceptBy(profileRepo.GetAll()
                            .Where(p => p.OwnerId == userId)
                            .Select(p => p.Id),
                        l => l.ProfileId));

                applications = applications.Except(applications.ExceptBy(
                    listings.Select(l => l.Id), a => a.ListingId));

                return Ok(applications);
        }
    }

    // POST: api/Application
    [HttpPost]
    public async Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto)
    {
        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/Application/{id}
    [HttpPut("{listingId}/{sitterId}")]
    public async Task<IActionResult> UpdateApplication(int listingId,
        int sitterId,
        [FromBody] UpdateApplicationDto updateDto)
    {
        var response =
            await _repo.UpdateAsync(listingId, sitterId, updateDto);
        return Ok(response);
    }

    // DELETE: api/Application/{id}
    [HttpDelete("{listingId}/{sitterId}")]
    public async Task<IActionResult> DeleteApplication(int listingId,
        int sitterId)
    {
        await _repo.DeleteAsync(listingId, sitterId);
        return Ok();
    }
}