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

    // https://localhost:7134/api/HouseListing
    [HttpGet]
    public async Task<IActionResult> GetAllHouseListings()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseListing: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
        
    // GET: api/HouseListing/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseListing(int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching HouseListing: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
                
        }
    }
    
    // POST: api/houseprofile
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

    // PUT: api/houseowner/{id}
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

    // DELETE: api/houseowner/{id}
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