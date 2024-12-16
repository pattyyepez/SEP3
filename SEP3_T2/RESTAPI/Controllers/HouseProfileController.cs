using DTOs.HouseOwner;
using DTOs.HouseProfile;
using DTOs.HouseSitter;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseProfileController : ControllerBase, IHouseProfileController
{
    private readonly IHouseProfileRepository _repo;

    public HouseProfileController(IHouseProfileRepository repo)
    {
        _repo = repo;
    }

    // Get: api/HouseProfile?includeOwner=true
    [HttpGet]
    public async Task<IActionResult> GetAllHouseProfiles(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromQuery] bool includeOwner)
    {
        var response = _repo.GetAll();
        if (!includeOwner) return Ok(response);

        var toReturn = new List<HouseProfileDto>();
        foreach (var houseProfile in response)
        {
            houseProfile.Owner =
                await ownerRepo.GetSingleAsync(houseProfile.OwnerId);
            toReturn.Add(houseProfile);
        }

        return Ok(toReturn.AsQueryable());
    }

    [HttpGet]
    public async Task<IActionResult> GetAllChores()
    {
        var response = _repo.GetAllChores();
        return Ok(response);
    }

    [HttpGet]
    public async Task<IActionResult> GetAllRules()
    {
        var response = _repo.GetAllRules();
        return Ok(response);
    }

    [HttpGet]
    public async Task<IActionResult> GetAllAmenities()
    {
        var response = _repo.GetAllAmenities();
        return Ok(response);
    }

    // GET: api/HouseProfile/{id}?includeOwner=true
    [HttpGet("{id}")]
    public async Task<IActionResult> Get(int id)
    {
        var response = await _repo.GetSingleAsync(id);

        return Ok(response);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetDetailed(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo, 
        [FromServices] IHouseReviewRepository reviewRepo,
        int id)
    {
        var response = await _repo.GetSingleAsync(id);
        
        var tempOwner = await ownerRepo.GetSingleAsync(response.OwnerId);
        response.Owner = new HouseOwnerDto
        {
            Name = tempOwner.Name,
            Biography = tempOwner.Biography,
            ProfilePicture = tempOwner.ProfilePicture
        };

        response.Reviews = reviewRepo.GetAll().Where(r => r.ProfileId == response.Id).ToList();

        foreach (var review in response.Reviews)
        {
            var tempSitter = await sitterRepo.GetSingleAsync(review.SitterId);
            review.Sitter = new HouseSitterDto
            {
                UserId = tempSitter.UserId,
                Name = tempSitter.Name,
                ProfilePicture = tempSitter.ProfilePicture
            };
        }

        return Ok(response);
    }

    [HttpGet("{ownerId}")]
    public async Task<IActionResult> GetByOwner(
        [FromRoute] int ownerId)
    {
        var profiles = _repo.GetAll()
            .Where(p => p.OwnerId == ownerId)
            .Select(p => new HouseProfileDto
            {
                Id = p.Id,
                Title = p.Title,
                Pictures = new List<string>(){p.Pictures[0]}
            });

        return Ok(profiles);
    }

    // POST: api/HouseProfile
    [HttpPost]
    public async Task<IActionResult> CreateHouseProfile(
        [FromBody] CreateHouseProfileDto createDto)
    {
        if (createDto.Pictures == null || createDto.Pictures.Count < 3)
            throw new Exception(
                "You need to upload at least 3 images when creating a house profile.");

        if (createDto.Amenities == null || createDto.Amenities!.Count == 0)
            throw new Exception(
                "You need to select at least 1 amenity when creating a house profile.");

        if (createDto.Chores == null || createDto.Chores!.Count == 0)
            throw new Exception(
                "You need to select at least 1 chore when creating a house profile.");

        if (createDto.Rules == null || createDto.Rules!.Count == 0)
            throw new Exception(
                "You need to select at least 1 rule when creating a house profile.");

        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/HouseProfile/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseProfile(int id,
        [FromBody] UpdateHouseProfileDto updateDto)
    {
        if (updateDto.Pictures == null || updateDto.Pictures.Count < 3)
            throw new Exception(
                "You need to upload at least 3 images when creating a house profile.");

        if (updateDto.Amenities == null || updateDto.Amenities!.Count == 0)
            throw new Exception(
                "You need to select at least 1 amenity when creating a house profile.");

        if (updateDto.Chores == null || updateDto.Chores!.Count == 0)
            throw new Exception(
                "You need to select at least 1 chore when creating a house profile.");

        if (updateDto.Rules == null || updateDto.Rules!.Count == 0)
            throw new Exception(
                "You need to select at least 1 rule when creating a house profile.");

        var response = await _repo.UpdateAsync(id, updateDto);
        return Ok(response);
    }

    // DELETE: api/HouseProfile/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseProfile(int id)
    {
        await _repo.DeleteAsync(id);
        return Ok();
    }
}