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

    // https://localhost:7134/api/Report
    [HttpGet]
    public async Task<IActionResult> GetAllReports()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all Reports:" +
                                   $" {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }

    // GET: api/Report/{id}
    [HttpGet("{id}")]
    public async Task<IActionResult> GetReport(int id)
    {
        try
        {
            var response = await _repo.GetSingleAsync(id);
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