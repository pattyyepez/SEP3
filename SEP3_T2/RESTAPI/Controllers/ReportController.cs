using DTOs.HouseOwner;
using DTOs.HouseSitter;
using DTOs.Report;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ReportController : ControllerBase
{
    private readonly IReportRepository _repo;

    public ReportController(IReportRepository repo)
    {
        _repo = repo;
    }

    // GET: api/Report?includeReporting=true&includeReported=true
    [HttpGet]
    public async Task<IActionResult> GetAllReports(
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeReporting,
        [FromQuery] bool includeReported)
    {
        try
        {
            var response = _repo.GetAll();
            if(!includeReporting && !includeReported) return Ok(response);
            var owners = ownerRepo.GetAll();
            var sitters = sitterRepo.GetAll();
            
            var toReturn = new List<ReportDto>();
            foreach (var report in response)
            {
                if (includeReporting)
                {
                    if(owners.Any(o => o.UserId == report.ReportingId))
                        report.ReportingOwner = owners.First(o => o.UserId == report.ReportingId);
                    else
                        report.ReportingSitter = sitters.First(s => s.UserId == report.ReportingId);
                }

                if (includeReported)
                {
                    if(owners.Any(o => o.UserId == report.ReportedId))
                        report.ReportedOwner = owners.First(o => o.UserId == report.ReportedId);
                    else
                        report.ReportedSitter = sitters.First(s => s.UserId == report.ReportedId);
                }
                
                toReturn.Add(report);
            }
            return Ok(toReturn.AsQueryable());
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all Reports:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/Report/{id}?includeReporting=true&includeReported=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetReport(int id,
        [FromServices] IHouseOwnerRepository ownerRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeReporting,
        [FromQuery] bool includeReported)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
            
            var owners = ownerRepo.GetAll();
            var sitters = sitterRepo.GetAll();

            if (includeReporting)
            {
                if(owners.Any(o => o.UserId == response.ReportingId))
                    response.ReportingOwner = owners.First(o => o.UserId == response.ReportingId);
                else
                    response.ReportingSitter = sitters.First(s => s.UserId == response.ReportingId);
            }

            if (includeReported)
            {
                if(owners.Any(o => o.UserId == response.ReportedId))
                    response.ReportedOwner = owners.First(o => o.UserId == response.ReportedId);
                else
                    response.ReportedSitter = sitters.First(s => s.UserId == response.ReportedId);
            }
            
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error fetching Report: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // POST: api/Report
    [HttpPost]
    public async Task<IActionResult> CreateReport(
        [FromBody] CreateReportDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating Report: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/Report/{id}
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateReport(int id,
        [FromBody] UpdateReportDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(id, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating Report: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/Report/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteReport(int id)
    {
        try
        {
            await _repo.DeleteAsync(id);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error deleting Report: {ex.Message}");
        }
    }
}