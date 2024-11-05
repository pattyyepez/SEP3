using DTOs.Application;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ApplicationController : ControllerBase
{
    private readonly IApplicationRepository _repo;

    public ApplicationController(IApplicationRepository repo)
    {
        _repo = repo;
    }
    
    // GET: api/HouseListing
    [HttpGet]
    public async Task<IActionResult> GetAllApplications()
    {
        try
        {
            var response = _repo.GetAll();
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching all Applications: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
        }
    }
    
    // GET: api/Application/{id}
    [HttpGet("{listingId}/{sitterId}")]
    public async Task<IActionResult> GetApplication(int listingId, int sitterId)
    {
        try
        {
            var response = await _repo.GetSingleAsync(listingId, sitterId);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Error fetching Application: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
                
        }
    }
    
    // POST: api/Application
    [HttpPost]
    public async Task<IActionResult> CreateApplication(
        [FromBody] CreateApplicationDto createDto)
    {
        try
        {
            var response = await _repo.AddAsync(createDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error creating Application: {ex.Message} \n{ex.InnerException} \n{ex.StackTrace}");
        }
    }

    // PUT: api/Application/{id}
    [HttpPut("{listingId}/{sitterId}")]
    public async Task<IActionResult> UpdateApplication(int listingId, int sitterId,
        [FromBody] UpdateApplicationDto updateDto)
    {
        try
        {
            var response = await _repo.UpdateAsync(listingId, sitterId, updateDto);
            return Ok(response);
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error updating HouseListing: {ex.Message}, {ex.InnerException}, {ex.StackTrace}");
        }
    }

    // DELETE: api/Application/{id}
    [HttpDelete("{listingId}/{sitterId}")]
    public async Task<IActionResult> DeleteApplication(int listingId, int sitterId)
    {
        try
        {
            await _repo.DeleteAsync(listingId, sitterId);
            return Ok();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting Application: {ex.Message}");
        }
    }
}