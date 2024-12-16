using DTOs.HouseOwner;
using DTOs.SitterReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface ISitterReviewController
{
    // GET: api/SitterReview?includeOwner=true&includeSitter=true
    Task<IActionResult> GetAll(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter);

    // GET: api/SitterReview/{ownerId}/{sitterId}?includeOwner=true&includeSitter=true
    Task<IActionResult> Get(int ownerId, int sitterId,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeOwner,
        [FromQuery] bool includeSitter);

    // POST: api/SitterReview/Create
    Task<IActionResult> Create(
        [FromBody] CreateSitterReviewDto createDto);
    
    // PUT : api/sitterReview/Update
    Task<IActionResult> Update(
        [FromBody] UpdateSitterReviewDto updateDto);

    // DELETE: api/SitterReview/Delete/{ownerId}/{sitterId}
    Task<IActionResult> DeleteSitterReview(int ownerId,
        int sitterId);
}