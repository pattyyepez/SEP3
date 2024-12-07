using DTOs.HouseOwner;
using DTOs.HouseProfile;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseProfileController : ControllerBase
{
    private readonly IHouseProfileRepository _repo;

    public HouseProfileController(IHouseProfileRepository repo)
    {
        _repo = repo;
    }

    // Get: api/HouseProfile?includeOwner=true
    [HttpGet]
    public async Task<IActionResult> GetAllHouseProfiles(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromQuery] bool includeOwner)
    {
        try
        {
            var response = _repo.GetAll();
            if (!includeOwner) return Ok(response);
            
            var toReturn = new List<HouseProfileDto>();
            foreach (var houseProfile in response)
            {
                houseProfile.Owner =
                    await ownerRepo.GetSingleAsync(houseProfile.OwnerId);
                toReturn.Add(houseProfile);
            }

            return Ok(toReturn.AsQueryable());

        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseProfiles:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    [HttpGet]
    public async Task<IActionResult> GetAllChores()
    {
        try
        {
            var response = _repo.GetAllChores();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseProfile Chores:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    [HttpGet]
    public async Task<IActionResult> GetAllRules()
    {
        try
        {
            var response = _repo.GetAllRules();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseProfile Rules:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    [HttpGet]
    public async Task<IActionResult> GetAllAmenities()
    {
        try
        {
            var response = _repo.GetAllAmenities();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseProfile Amenities:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/HouseProfile/{id}?includeOwner=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseProfile(int id,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromQuery] bool includeOwner)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);

            if (includeOwner)
            {
                // var temp = await ownerRepo.GetSingleAsync(response.OwnerId);
                // response.Owner = new HouseOwnerDto
                // {
                //     Name = temp.Name,
                //     
                // }
                response.Owner = await ownerRepo.GetSingleAsync(response.OwnerId);
            }
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseProfile: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    [HttpGet("OwnerId")]
    public async Task<IActionResult> GetProfilesByOwner([FromQuery] int? ownerId)
    {
        try
        {
            IQueryable<HouseProfileDto> profiles = _repo.GetAll();

            if (ownerId.HasValue)
            {
                profiles = profiles.Where(p => p.OwnerId == ownerId.Value);
            }

            return Ok(profiles);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseProfile: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }

    }

    // POST: api/HouseProfile
    [HttpPost]
    public async Task<IActionResult> CreateHouseProfile(
        [FromBody] CreateHouseProfileDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating HouseProfile: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/HouseProfile/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseProfile(int id,
        [FromBody] UpdateHouseProfileDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(id, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating HouseProfile: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/HouseProfile/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseProfile(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting HouseProfile: {ex.Message}");
        }
    }
}