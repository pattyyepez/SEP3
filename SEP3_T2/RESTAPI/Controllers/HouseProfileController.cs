using DTOs.HouseProfile;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class HouseProfileController : ControllerBase
{
    private readonly IHouseProfileRepository _repo;

    public HouseProfileController(IHouseProfileRepository repo)
    {
        _repo = repo;
    }

    // https://localhost:7134/api/HouseProfile
    [HttpGet]
    public async Task<IActionResult> GetAllHouseProfiles()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseProfiles:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/houseowner/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseProfile(int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseProfile: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/houseprofile
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

    // PUT: api/houseowner/{id}
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

    // DELETE: api/houseowner/{id}
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