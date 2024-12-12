using DTOs.HouseOwner;
using DTOs.SitterReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class SitterReviewController : ControllerBase, ISitterReviewController
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
        var response = _repo.GetAll();
        if (!includeOwner && !includeSitter) return Ok(response);

        var toReturn = new List<SitterReviewDto>();
        foreach (var review in response)
        {
            if (includeOwner)
                review.Owner = await ownerRepo.GetSingleAsync(review.OwnerId);

            if (includeSitter)
                review.Sitter =
                    await sitterRepo.GetSingleAsync(review.SitterId);

            toReturn.Add(review);
        }

        return Ok(toReturn.AsQueryable());
    }

    // GET: api/SitterReview/{ownerId}/{sitterId}?includeOwner=true&includeSitter=true
    [HttpGet("{ownerId}/{sitterId}")]
    public async Task<IActionResult> Get(int ownerId, int sitterId,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter)
    {
        var response = await _repo.GetSingleAsync(ownerId, sitterId);

        if (includeOwner)
            response.Owner = await ownerRepo.GetSingleAsync(response.OwnerId);

        if (includeSitter)
            response.Sitter =
                await sitterRepo.GetSingleAsync(response.SitterId);

        return Ok(response);
    }

    // GET: api/SitterReview/GetAllForSitter/{sitterId}
    [HttpGet("{sitterId}")]
    public async Task<IActionResult> GetAllForSitter(
        [FromServices] IHouseOwnerRepository ownerRepo, int sitterId)
    {
        var response = _repo.GetAll().Where(r => r.SitterId == sitterId);

        foreach (var review in response)
        {
            var temp = await ownerRepo.GetSingleAsync(review.OwnerId);
            review.Owner = new HouseOwnerDto
            {
                ProfilePicture = temp.ProfilePicture,
                Name = temp.Name,
            };
        }

        return Ok(response);
    }

    [HttpGet("{sitterId}")]
    public IActionResult GetAverageForSitter(int sitterId)
    {
        var response = _repo.GetAll().Where(r => r.SitterId == sitterId)
            .Select(r => r.Rating).Average();
        return Ok(response);
    }

    // POST: api/SitterReview/Create
    [HttpPost]
    public async Task<IActionResult> Create(
        [FromBody] CreateSitterReviewDto createDto)
    {
        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT : api/sitterReview/Update
    [HttpPut]
    public async Task<IActionResult> Update(
        [FromBody] UpdateSitterReviewDto updateDto)
    {
        var response = await _repo.UpdateAsync(updateDto);
        return Ok(response);
    }

    // DELETE: api/SitterReview/Delete/{ownerId}/{sitterId}
    [HttpDelete("{ownerId}/{sitterId}")]
    public async Task<IActionResult> DeleteSitterReview(int ownerId,
        int sitterId)
    {
        await _repo.DeleteAsync(ownerId, sitterId);
        return Ok();
    }
}