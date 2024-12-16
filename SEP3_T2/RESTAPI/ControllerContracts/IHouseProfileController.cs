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
    Task<IActionResult> Get([FromRoute] int id);
    
    Task<IActionResult> GetDetailed(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromServices] IHouseReviewRepository reviewRepo,
        [FromRoute] int id);

    Task<IActionResult> GetByOwner(
        [FromRoute] int ownerId);

    // POST: api/HouseProfile
    Task<IActionResult> CreateHouseProfile(
        [FromBody] CreateHouseProfileDto createDto);

    // PUT: api/HouseProfile/{id}
    Task<IActionResult> UpdateHouseProfile(int id,
        [FromBody] UpdateHouseProfileDto updateDto);

    // DELETE: api/HouseProfile/{id}
    Task<IActionResult> DeleteHouseProfile(int id);
}