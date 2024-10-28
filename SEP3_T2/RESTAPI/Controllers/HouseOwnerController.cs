using DTOs;
using DTOs.HouseOwner;
using Entities;
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

        // GET: api/houseowner/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetHouseOwner(int id)
        {
            try
            {
                var response = await _repo.GetSingleAsync(id);

                var houseOwner = new HouseOwnerDTO
                {
                    OwnerId = response.OwnerId,
                    Address = response.Address,
                    Biography = response.Biography
                };

                return Ok(houseOwner);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error fetching HouseOwner: {ex.Message}");
            }
        }

        // POST: api/houseowner
        [HttpPost]
        public async Task<IActionResult> CreateHouseOwner([FromBody] CreateHouseOwnerDTO createDto)
        {
            try
            {
                var request = new HouseOwner()
                {
                    Address = createDto.Address,
                    Biography = createDto.Biography
                };

                var response = await _repo.AddAsync(request);
                return Ok(response);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error creating HouseOwner: {ex.Message}");
            }
        }

        // PUT: api/houseowner/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateHouseOwner(int id, [FromBody] CreateHouseOwnerDTO updateDto)
        {
            try
            {
                var request = new HouseOwner
                {
                    OwnerId = id,
                    Address = updateDto.Address,
                    Biography = updateDto.Biography
                };

                var response = await _repo.UpdateAsync(request);
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
