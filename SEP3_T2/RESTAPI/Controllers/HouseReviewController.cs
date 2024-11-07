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
    
    // Get: api/HouseReview?includeProfile=true&includeSitter=true
    [HttpGet]
    public async Task<IActionResult> GetAllHouseReviews(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = _repo.GetAll();
            if(!includeSitter && !includeProfile) return Ok(response);
            
            var toReturn = new List<HouseReviewDto>();
            foreach (var review in response)
            {
                if (includeProfile)
                    review.Profile = await profileRepo.GetSingleAsync(review.ProfileId);
                
                if(includeSitter)
                    review.Sitter = await sitterRepo.GetSingleAsync(review.SitterId);
                
                toReturn.Add(review);
            }
            return Ok(toReturn.AsQueryable());
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all HouseReviews:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/HouseReview/{id}?includeProfile=true&includeSitter=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseReview(int id,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            
            if(includeProfile)
                response.Profile = await profileRepo.GetSingleAsync(response.ProfileId);
            
            if(includeSitter)
                response.Sitter = await sitterRepo.GetSingleAsync(response.SitterId);
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching HouseReview: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/HouseReview
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

    // DELETE: api/HouseReview/{id}
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