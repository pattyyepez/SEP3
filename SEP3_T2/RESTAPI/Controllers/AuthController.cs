using DTOs.HouseOwner;
using DTOs.HouseSitter;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class AuthController : ControllerBase
{
    private readonly IHouseOwnerRepository _ownerRepo;
    private readonly IHouseSitterRepository _sitterRepo;

    public AuthController(IHouseOwnerRepository ownerRepo, IHouseSitterRepository sitterRepo)
    {
        _ownerRepo = ownerRepo;
        _sitterRepo = sitterRepo;
    }

    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] LoginRequest request)
    {
        HouseOwnerDto? owner = null;
        HouseSitterDto? sitter = null;

        if(_ownerRepo.GetAll().Any(o => o.Email == request.Email))
        {
            owner = _ownerRepo.GetAll().SingleOrDefault(o => o.Email == request.Email);
        }
        else
        {
            sitter = _sitterRepo.GetAll().SingleOrDefault(s => s.Email == request.Email);
        }

        if (owner != null && owner.Password == request.Password)
            return Ok(owner);
        
        if (sitter != null && sitter.Password == request.Password)
            return Ok (sitter);
        
        return Unauthorized("Invalid email or password.");
    }
}