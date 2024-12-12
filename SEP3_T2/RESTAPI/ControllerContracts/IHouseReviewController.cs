using DTOs.HouseReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface IHouseReviewController
{
    // Get: api/HouseReview?includeProfile=true&includeSitter=true
    Task<IActionResult> GetAll(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter);

    // GET: api/HouseReview/{id}?includeProfile=true&includeSitter=true
    Task<IActionResult> Get(int profileId, int sitterId,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeProfile,
        [FromQuery] bool includeSitter);
    
    // GET: api/SitterReview/{sitterId}
    Task<IActionResult> GetAllForProfile(
        [FromServices] IHouseSitterRepository sitterRepo,
        int profileId);

    // POST: api/HouseReview
    Task<IActionResult> Create(
        [FromBody] CreateHouseReviewDto createDto);

    // PUT : api/houseReview/Update
    Task<IActionResult> Update(
        [FromBody] UpdateHouseReviewDto updateDto);

    // DELETE: api/HouseReview/{id}
    Task<IActionResult> Delete(int profileId, int sitterId);
}