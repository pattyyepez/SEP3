using DTOs.HouseProfile;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface IHouseProfileController
{
    // Get: api/HouseProfile?includeOwner=true
    Task<IActionResult> GetAllHouseProfiles(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromQuery] bool includeOwner);

    Task<IActionResult> GetAllChores();

    Task<IActionResult> GetAllRules();

    Task<IActionResult> GetAllAmenities();

    // GET: api/HouseProfile/{id}?includeOwner=true
    Task<IActionResult> GetHouseProfile(int id,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromQuery] bool includeOwner);

    Task<IActionResult> GetProfilesByOwner(
        [FromQuery] int? ownerId);

    // POST: api/HouseProfile
    Task<IActionResult> CreateHouseProfile(
        [FromBody] CreateHouseProfileDto createDto);

    // PUT: api/HouseProfile/{id}
    Task<IActionResult> UpdateHouseProfile(int id,
        [FromBody] UpdateHouseProfileDto updateDto);

    // DELETE: api/HouseProfile/{id}
    Task<IActionResult> DeleteHouseProfile(int id);
}