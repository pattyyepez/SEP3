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
    public async Task<IActionResult> GetAll(
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

    // GET: api/SitterReview/{ownerId}/{sitterId}?includeOwner=true&includeSitter=true
    [HttpGet("{ownerId}/{sitterId}")]
    public async Task<IActionResult> Get(int ownerId, int sitterId,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter)
    {
        try
        {
            var response = await _repo.GetSingleAsync(ownerId, sitterId);

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
    
    // GET: api/SitterReview/GetAllForSitter/{sitterId}
    [HttpGet("{sitterId}")]
    public async Task<IActionResult> GetAllForSitter([FromServices] IHouseOwnerRepository ownerRepo, int sitterId)
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

    // POST: api/SitterReview/Create
    [HttpPost]
    public async Task<IActionResult> Create(
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
    
    // PUT : api/sitterReview/Update
    [HttpPut]
    public async Task<IActionResult> Update(
        [FromBody] UpdateSitterReviewDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error editing Sitter Review: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // DELETE: api/SitterReview/Delete/{ownerId}/{sitterId}
    [HttpDelete("{ownerId}/{sitterId}")]
    public async Task<IActionResult> DeleteSitterReview(int ownerId, int sitterId)
    {
        try
        {
            await _repo.DeleteAsync(ownerId, sitterId);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting SitterReview: {ex.Message}");
        }
    }
}