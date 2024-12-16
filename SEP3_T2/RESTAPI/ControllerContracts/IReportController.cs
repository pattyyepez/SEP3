using DTOs.Report;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

public interface IReportController
{
    // GET: api/Report?includeReporting=true&includeReported=true
    Task<IActionResult> GetAllReports(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeReporting,
        [FromQuery] bool includeReported);

    // GET: api/Report/{id}?includeReporting=true&includeReported=true
    Task<IActionResult> GetReport(int id,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeReporting,
        [FromQuery] bool includeReported);

    // POST: api/Report
    Task<IActionResult> CreateReport(
        [FromBody] CreateReportDto createDto);

    // PUT: api/Report/{id}
    Task<IActionResult> UpdateReport(int id,
        [FromBody] UpdateReportDto updateDto);

    // DELETE: api/Report/{id}
    Task<IActionResult> DeleteReport(int id);
}