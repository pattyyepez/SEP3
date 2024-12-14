using DTOs.Application;
using DTOs.HouseListing;
using DTOs.HouseProfile;
using DTOs.HouseReview;
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

    [HttpGet("{sitterId}")]
    public async Task<IActionResult> GetMyApplicationsSitter(
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseProfileRepository profileRepo, 
        [FromServices] IHouseReviewRepository reviewRepo,
        [FromRoute] int sitterId)
    {
        var response = _repo.GetAll().Where(a => a.SitterId == sitterId && a.Status != "Confirmed");

        foreach (var application in response)
        {
            application.Listing = await listingRepo.GetSingleAsync(application.ListingId);
            
            var tempProfile = await profileRepo.GetSingleAsync(application.Listing.ProfileId);
            application.Listing.Profile = new HouseProfileDto
            {
                Title = tempProfile.Title,
                Pictures = new List<string>{tempProfile.Pictures[0]}
            };

            var tempReviews = reviewRepo.GetAll();
            application.Listing.Profile.Reviews = tempReviews
                .Where(r => r.ProfileId == tempProfile.Id)
                .Select(r => new HouseReviewDto{ Rating = r.Rating })
                .ToList();
        }
        
        var sortOrder = new Dictionary<string, byte>() {
            { "Approved", 0 },
            { "Pending", 1 },
            { "Rejected", 2 },
            { "Canceled", 3 } 
        };

        response = response.OrderBy(x => sortOrder[x.Status]);

        return Ok(response);
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

    // POST: api/Application
    [HttpPost]
    public async Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto)
    {
        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/Application/{id}
    [HttpPut]
    public async Task<IActionResult> UpdateApplication([FromBody] UpdateApplicationDto updateDto)
    {
        var response =
            await _repo.UpdateAsync(updateDto);
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