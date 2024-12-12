using DTOs.HouseSitter;
using RepositoryContracts;

namespace RESTAPI.ControllerContracts;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

public interface IHouseSitterController
{
    // GET: api/HouseSitter
    Task<IActionResult> GetAllHouseSitters();
    
    // GET: api/HouseSitter
    Task<IActionResult> GetAllSkills();

    // GET: api/HouseSitter/{id}
    Task<IActionResult> GetHouseSitter(int id);

    // POST: api/HouseSitter
    Task<IActionResult> CreateHouseSitter(
        [FromBody] CreateHouseSitterDto createDto);

    // PUT: api/HouseSitter/{id}
    Task<IActionResult> UpdateHouseSitter(int id,
        [FromBody] UpdateHouseSitterDto updateDto);

    // DELETE: api/HouseSitter/{id}
    Task<IActionResult> DeleteHouseSitter(int id);
}