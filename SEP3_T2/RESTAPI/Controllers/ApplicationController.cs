using DTOs.Application;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
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
            if(!includeSitter && !includeListing) return Ok(response);
            
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
            return StatusCode(500, $"Error fetching all Applications: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
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
            return StatusCode(500, $"Error fetching Application: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
                
        }
    }
    
    [HttpGet("SitterId")]
    public async Task<IActionResult> GetApplicationsBySitter([FromQuery] int? sitterId)
    {
        try
        {
            IQueryable<ApplicationDto> applications = _repo.GetAll();

            if (sitterId.HasValue)
            {
                applications = applications.Where(a => a.SitterId == sitterId.Value);
            }

            return Ok(applications);
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
            return StatusCode(500,
                $"Error creating Application: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/Application/{id}
    [HttpPut("{listingId}/{sitterId}")]
    public async Task<IActionResult> UpdateApplication(int listingId, int sitterId,
        [FromBody] UpdateApplicationDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(listingId, sitterId, updateDto);
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
    public async Task<IActionResult> DeleteApplication(int listingId, int sitterId)
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