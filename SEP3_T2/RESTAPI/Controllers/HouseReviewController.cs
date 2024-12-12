using DTOs.HouseReview;
using DTOs.HouseSitter;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseReviewController : ControllerBase, IHouseReviewController
{
    private readonly IHouseReviewRepository _repo;

    public HouseReviewController(IHouseReviewRepository repo)
    {
        _repo = repo;
    }

    // Get: api/HouseReview?includeProfile=true&includeSitter=true
    [HttpGet]
    public async Task<IActionResult> GetAll(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter)
    {
        var response = _repo.GetAll();
        if (!includeSitter && !includeProfile) return Ok(response);

        var toReturn = new List<HouseReviewDto>();
        foreach (var review in response)
        {
            if (includeProfile)
                review.Profile =
                    await profileRepo.GetSingleAsync(review.ProfileId);

            if (includeSitter)
                review.Sitter =
                    await sitterRepo.GetSingleAsync(review.SitterId);

            toReturn.Add(review);
        }

        return Ok(toReturn.AsQueryable());
    }

    // GET: api/HouseReview/{id}?includeProfile=true&includeSitter=true
    [HttpGet("{profileId}/{sitterId}")]
    public async Task<IActionResult> Get(int profileId, int sitterId,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter)
    {
        var response = await _repo.GetSingleAsync(profileId, sitterId);

        if (includeProfile)
            response.Profile =
                await profileRepo.GetSingleAsync(response.ProfileId);

        if (includeSitter)
            response.Sitter =
                await sitterRepo.GetSingleAsync(response.SitterId);

        return Ok(response);
    }

    // GET: api/SitterReview/{sitterId}
    [HttpGet("{profileId}")]
    public async Task<IActionResult> GetAllForProfile(
        [FromServices] IHouseSitterRepository sitterRepo,
        int profileId)
    {
        var response = _repo.GetAll().Where(r => r.ProfileId == profileId);

        foreach (var review in response)
        {
            var temp = await sitterRepo.GetSingleAsync(review.SitterId);
            review.Sitter = new HouseSitterDto
            {
                ProfilePicture = temp.ProfilePicture,
                Name = temp.Name,
            };
        }

        return Ok(response);
    }

    // POST: api/HouseReview
    [HttpPost]
    public async Task<IActionResult> Create(
        [FromBody] CreateHouseReviewDto createDto)
    {
        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT : api/houseReview/Update
    [HttpPut]
    public async Task<IActionResult> Update(
        [FromBody] UpdateHouseReviewDto updateDto)
    {
        var response = await _repo.UpdateAsync(updateDto);
        return Ok(response);
    }

    // DELETE: api/HouseReview/{id}
    [HttpDelete("{profileId}/{sitterId}")]
    public async Task<IActionResult> Delete(int profileId, int sitterId)
    {
        await _repo.DeleteAsync(profileId, sitterId);
        return Ok();
    }
}