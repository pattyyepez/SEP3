using DTOs.SitterReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SitterReviewController : ControllerBase
{
    private readonly ISitterReviewRepository _repo;

    public SitterReviewController(ISitterReviewRepository repo)
    {
        _repo = repo;
    }
    
    // https://localhost:7134/api/SitterReview
    [HttpGet]
    public async Task<IActionResult> GetAllSitterReviews()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all SitterReviews:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/sitterReview/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetSitterReview(int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching SitterReview: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/sitterReview
    [HttpPost]
    public async Task<IActionResult> CreateSitterProfile(
        [FromBody] CreateSitterReviewDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating SitterReview: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // DELETE: api/sitterReview/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteSitterReview(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting SitterReview: {ex.Message}");
        }
    }
}