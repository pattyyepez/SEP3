using DTOs.HouseOwner;
using DTOs.SitterReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class SitterReviewController : ControllerBase
{
    private readonly ISitterReviewRepository _repo;

    public SitterReviewController(ISitterReviewRepository repo)
    {
        _repo = repo;
    }
    
    // GET: api/SitterReview?includeOwner=true&includeSitter=true
    [HttpGet]
    public async Task<IActionResult> GetAllSitterReviews(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = _repo.GetAll();
            if(!includeOwner && !includeSitter) return Ok(response);

            var toReturn = new List<SitterReviewDto>();
            foreach (var review in response)
            {
                if (includeOwner) 
                    review.Owner = await ownerRepo.GetSingleAsync(review.OwnerId);
            
                if(includeSitter)
                    review.Sitter = await sitterRepo.GetSingleAsync(review.SitterId);
                
                toReturn.Add(review);
            }
            
            return Ok(toReturn.AsQueryable());
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all SitterReviews:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/SitterReview/{id}?includeOwner=true&includeSitter=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetSitterReview(int id,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);

            if (includeOwner) 
                response.Owner = await ownerRepo.GetSingleAsync(response.OwnerId);
            
            if(includeSitter)
                response.Sitter = await sitterRepo.GetSingleAsync(response.SitterId);
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching SitterReview: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    // GET: api/SitterReview/{sitterId}
    [HttpGet("{sitterId}")]
    public async Task<IActionResult> GetAllReviewsForSitter([FromServices] IHouseOwnerRepository ownerRepo, int sitterId)
    {
        try
        {
            var response = _repo.GetAll().Where(r => r.SitterId == sitterId);

            foreach (var review in response)
            {
                var temp = await ownerRepo.GetSingleAsync(review.OwnerId);
                review.Owner = new HouseOwnerDto
                {
                    Name = temp.Name,
                };
            }
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching SitterReview: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/SitterReview
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

    // DELETE: api/SitterReview/{id}
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