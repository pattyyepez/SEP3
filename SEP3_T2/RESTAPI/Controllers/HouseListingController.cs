using DTOs.HouseListing;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class HouseListingController : ControllerBase
{
    private readonly IHouseListingRepository _repo;

    public HouseListingController(IHouseListingRepository repo)
    {
        _repo = repo;
    }

    // Get: api/HouseListing?includeProfile=true
    [HttpGet]
    public async Task<IActionResult> GetAllHouseListings(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile)
    {
        try
        {
            var response = _repo.GetAll();
            if (!includeProfile) return Ok(response);
            
            var toReturn = new List<HouseListingDto>();
            foreach (var listing in response)
            {
                listing.Profile = await profileRepo.GetSingleAsync(listing.ProfileId);
                toReturn.Add(listing);
            }
            
            return Ok(toReturn.AsQueryable());
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseListing: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
        
    // GET: api/HouseListing/{id}?includeProfile=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseListing(int id,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);

            if (includeProfile)
                response.Profile = await profileRepo.GetSingleAsync(response.ProfileId);
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching HouseListing: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
                
        }
    }
    
    [HttpGet("ProfileId")]
    public async Task<IActionResult> GetListingsByProfile([FromQuery] int? profileId)
    {
        try
        {
            IQueryable<HouseListingDto> listings = _repo.GetAll();

            if (profileId.HasValue)
            {
                listings = listings.Where(l => l.ProfileId == profileId.Value);
            }

            return Ok(listings);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseListing: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }

    }
    
    // POST: api/HouseListing
    [HttpPost]
    public async Task<IActionResult> CreateHouseListing(
        [FromBody] CreateHouseListingDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating HouseListing: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/HouseListing/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseListing(int id,
        [FromBody] UpdateHouseListingDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(id, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating HouseListing: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/HouseListing/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseListing(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting HouseListing: {ex.Message}");
        }
    }
}