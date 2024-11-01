using DTOs;
using DTOs.HouseSitter;
using RepositoryContracts;

namespace RESTAPI.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

[ApiController]
    [Route("api/[controller]")]
    public class HouseSitterController : ControllerBase
    {
        private readonly IHouseSitterRepository _repo;

        public HouseSitterController(IHouseSitterRepository repo)
        {
            _repo = repo;
        }

        // GET: api/houseowner/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetHouseSitter(int id)
        {
            try
            {
                var response = await _repo.GetSingleAsync(id);

                // var houseSitter = new HouseSitterDTO
                // {
                //     UserId = response.UserId,
                //     Address = response.Address,
                //     Biography = response.Biography
                // };

                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error fetching HouseSitter: {ex.Message}\n{ex.InnerException}\n{ex.StackTrace}");
                
            }
        }

        // POST: api/houseowner
        [HttpPost]
        public async Task<IActionResult> CreateHouseSitter([FromBody] CreateHouseSitterDTO createDto)
        {
            try
            {
                var response = await _repo.AddAsync(createDto);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error creating HouseSitter: {ex.Message}");
            }
        }

        // PUT: api/houseowner/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateHouseSitter(int id, [FromBody] UpdateHouseSitterDTO updateDto)
        {
            try
            {
                var response = await _repo.UpdateAsync(id, updateDto);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error updating HouseSitter: {ex.Message}");
            }
        }

        // DELETE: api/houseowner/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteHouseSitter(int id)
        {
            try
            {
                await _repo.DeleteAsync(id);
                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error deleting HouseSitter: {ex.Message}");
            }
        }
    }
