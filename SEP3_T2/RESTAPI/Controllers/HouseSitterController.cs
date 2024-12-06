using DTOs;
using DTOs.HouseSitter;
using RepositoryContracts;

namespace RESTAPI.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseSitterController : ControllerBase
{
    private readonly IHouseSitterRepository _repo;

    public HouseSitterController(IHouseSitterRepository repo)    
    {
        _repo = repo;
    }

    // GET: api/HouseSitter
    [HttpGet]
    public async Task<IActionResult> GetAllHouseSitters()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseSitters:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    // GET: api/HouseSitter
    [HttpGet]
    public async Task<IActionResult> GetAllSkills()
    {
        try
        {
            var response = _repo.GetAllSkills();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseSitters Skills:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/HouseSitter/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseSitter(
        [FromServices] ISitterReviewRepository reviewRepo,
        [FromQuery] bool includeReviews,
        int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            
            if (includeReviews)
                response.Reviews = reviewRepo.GetAll().Where(r => r.SitterId == id).ToList();
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseSitter: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/HouseSitter
    [HttpPost]
    public async Task<IActionResult> CreateHouseSitter(
        [FromBody] CreateHouseSitterDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating HouseSitter: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/HouseSitter/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseSitter(int id,
        [FromBody] UpdateHouseSitterDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(id, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating HouseSitter: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/HouseSitter/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseSitter(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error deleting HouseSitter: {ex.Message}");
        }
    }
}