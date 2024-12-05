using DTOs.HouseOwner;
using RepositoryContracts;

namespace RESTAPI.Controllers;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

[ApiController]
    [Route("api/[controller]")]
    public class HouseOwnerController : ControllerBase
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
            try
            {
                var response = _repo.GetAll();
                return Ok(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                Console.WriteLine(ex.StackTrace);
                Console.WriteLine(ex.InnerException);
                return StatusCode(500, $"Error fetching all HouseOwners: {ex.Message}");
            }
        }
        
        // GET: api/houseowner/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetHouseOwner(int id)
        {
            try
            {
                var response = await _repo.GetSingleAsync(id);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error fetching HouseOwner: {ex.Message}");
                
            }
        }

        // POST: api/houseowner
        [HttpPost]
        public async Task<IActionResult> CreateHouseOwner([FromBody] CreateHouseOwnerDto createDto)
        {
            try
            {
                var response = await _repo.AddAsync(createDto);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error creating HouseOwner: {ex.Message}");
            }
        }

        // PUT: api/houseowner/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateHouseOwner(int id, [FromBody] UpdateHouseOwnerDto updateDto)
        {
            try
            {
                var response = await _repo.UpdateAsync(id, updateDto);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error updating HouseOwner: {ex.Message}");
            }
        }

        // DELETE: api/houseowner/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteHouseOwner(int id)
        {
            try
            {
                await _repo.DeleteAsync(id);
                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error deleting HouseOwner: {ex.Message}");
            }
        }
    }
