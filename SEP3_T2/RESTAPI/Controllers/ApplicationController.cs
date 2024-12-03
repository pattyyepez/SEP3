using DTOs.Application;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class ApplicationController : ControllerBase
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
        try
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
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching all Applications: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/Application/{id}?includeListing=true&includeSitter=true
    [HttpGet("{listingId}/{sitterId}")]
    public async Task<IActionResult> GetApplication(int listingId, int sitterId,
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeListing,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = await _repo.GetSingleAsync(listingId, sitterId);

            if (includeListing)
                response.Listing = await listingRepo.GetSingleAsync(listingId);

            if (includeSitter)
                response.Sitter = await sitterRepo.GetSingleAsync(sitterId);

            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching Application: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    // GET: https://localhost:7134/api/Application/GetApplication/{listingId}
    [HttpGet("{listingId}")]
    public async Task<IActionResult> GetApplication(int listingId)
    {
        try
        {
            var response = _repo.GetAll().Where(a => a.ListingId == listingId);

            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching Application: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
// GET https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}
    [HttpGet("{userId}/{status}")]
    public async Task<IActionResult> GetApplicationsByUser(
        [FromServices] IHouseListingRepository listingRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        int? userId, string? status)
    {
        try
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
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching Application: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // POST: api/Application
    [HttpPost]
    public async Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            Console.WriteLine(
                $"Error creating Application: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");

            return StatusCode(500,
                $"Error creating Application: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/Application/{id}
    [HttpPut("{listingId}/{sitterId}")]
    public async Task<IActionResult> UpdateApplication(int listingId,
        int sitterId,
        [FromBody] UpdateApplicationDto updateDto)
    {
        try
        {
            var response =
                await _repo.UpdateAsync(listingId, sitterId, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating HouseListing: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/Application/{id}
    [HttpDelete("{listingId}/{sitterId}")]
    public async Task<IActionResult> DeleteApplication(int listingId,
        int sitterId)
    {
        try
        {
            await _repo.DeleteAsync(listingId, sitterId);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting Application: {ex.Message}");
        }
    }
}