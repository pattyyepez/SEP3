using DTOs.HouseReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class HouseReviewController : ControllerBase
{
    private readonly IHouseReviewRepository _repo;

    public HouseReviewController(IHouseReviewRepository repo)
    {
        _repo = repo;
    }
    
    // https://localhost:7134/api/HouseReview
    [HttpGet]
    public async Task<IActionResult> GetAllHouseReviews()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseReviews:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/houseReview/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseReview(int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseReview: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/houseReview
    [HttpPost]
    public async Task<IActionResult> CreateHouseProfile(
        [FromBody] CreateHouseReviewDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating HouseReview: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // DELETE: api/houseReview/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseReview(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting HouseReview: {ex.Message}");
        }
    }
}