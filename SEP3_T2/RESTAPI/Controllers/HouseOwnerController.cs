using DTOs.HouseOwner;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

[ApiController]
[Route("api/[controller]")]
public class HouseOwnerController : ControllerBase, IHouseOwnerController
{
    private readonly IHouseOwnerRepository _repo;

    public HouseOwnerController(IHouseOwnerRepository repo)
    {
        _repo = repo;
    }

    // https://localhost:7134/api/HouseOwner
    [HttpGet]
    public async Task<IActionResult> GetAllHouseOwners()
    {
        var response = _repo.GetAll();
        return Ok(response);
    }

    // GET: api/houseowner/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseOwner(int id)
    {
        var response = await _repo.GetSingleAsync(id);
        return Ok(response);
    }

    // POST: api/houseowner
    [HttpPost]
    public async Task<IActionResult> CreateHouseOwner(
        [FromBody] CreateHouseOwnerDto createDto)
    {
        if (createDto.ProfilePicture == null ||
            string.IsNullOrWhiteSpace(createDto.ProfilePicture))
            throw new Exception(
                "You need to upload a profile picture when creating an account.");

        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/houseowner/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateHouseOwner(int id,
        [FromBody] UpdateHouseOwnerDto updateDto)
    {
        if (updateDto.ProfilePicture == null ||
            string.IsNullOrWhiteSpace(updateDto.ProfilePicture))
            throw new Exception(
                "You need to upload a profile picture when creating an account.");

        var response = await _repo.UpdateAsync(id, updateDto);
        return Ok(response);
    }

    // DELETE: api/houseowner/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseOwner(int id)
    {
        await _repo.DeleteAsync(id);
        return Ok();
    }
}