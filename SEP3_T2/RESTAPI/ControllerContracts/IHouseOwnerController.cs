using DTOs.HouseOwner;

namespace RESTAPI.ControllerContracts;

using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;


    public interface IHouseOwnerController
    {
        // https://localhost:7134/api/HouseOwner
        Task<IActionResult> GetAllHouseOwners();
        
        // GET: api/houseowner/{id}
        Task<IActionResult> GetHouseOwner(int id);

        // POST: api/houseowner
        Task<IActionResult> CreateHouseOwner(
            [FromBody] CreateHouseOwnerDto createDto);

        // PUT: api/houseowner/{id}
        Task<IActionResult> UpdateHouseOwner(int id,
            [FromBody] UpdateHouseOwnerDto updateDto);

        // DELETE: api/houseowner/{id}
        Task<IActionResult> DeleteHouseOwner(int id);
    }
