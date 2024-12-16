using DTOs.HouseOwner;
using DTOs.HouseSitter;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseSitterController : ControllerBase, IHouseSitterController
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
        var response = _repo.GetAll();
        return Ok(response);
    }

    // GET: api/HouseSitter
    [HttpGet]
    public async Task<IActionResult> GetAllSkills()
    {
        var response = _repo.GetAllSkills();
        return Ok(response);
    }

    // GET: api/HouseSitter/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseSitter(int id)
    {
        var response = await _repo.GetSingleAsync(id);
        return Ok(response);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetViewSitterProfile(
        [FromServices] ISitterReviewRepository reviewRepo,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromRoute] int id)
    {
        var response = await _repo.GetSingleAsync(id);
        response = new HouseSitterDto
        {
            Name = response.Name,
            Biography = response.Biography,
            Experience = response.Experience,
            Pictures = response.Pictures,
            Skills = response.Skills,
        };
        
        response.Reviews = reviewRepo.GetAll().Where(r => r.SitterId == id).ToList();
        foreach (var review in response.Reviews)
        {
            var tempOwner = await ownerRepo.GetSingleAsync(review.OwnerId);
            review.Owner = new HouseOwnerDto
            {
                Name = tempOwner.Name,
                ProfilePicture = tempOwner.ProfilePicture
            };
        }
        
        return Ok(response);
    }

    // POST: api/HouseSitter
    [HttpPost]
    public async Task<IActionResult> CreateHouseSitter(
        [FromBody] CreateHouseSitterDto createDto)
    {
        if (createDto.Pictures == null || createDto.Pictures.Count < 3)
            throw new Exception(
                "You need to upload at least 3 images when creating an account.");

        if (createDto.Skills == null || createDto.Skills!.Count == 0)
            throw new Exception(
                "You need to select at least 1 skill when creating an account.");

        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/HouseSitter/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseSitter(int id,
        [FromBody] UpdateHouseSitterDto updateDto)
    {
        if (updateDto.Pictures == null || updateDto.Pictures.Count < 3)
            throw new Exception(
                "You need to upload at least 3 images when updating an account.");

        if (updateDto.Skills == null || updateDto.Skills!.Count == 0)
            throw new Exception(
                "You need to select at least 1 skill when updating an account.");

        var response = await _repo.UpdateAsync(id, updateDto);
        return Ok(response);
    }

    // DELETE: api/HouseSitter/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseSitter(int id)
    {
        await _repo.DeleteAsync(id);
        return Ok();
    }
}