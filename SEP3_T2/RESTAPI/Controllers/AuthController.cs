using DTOs.HouseOwner;
using DTOs.HouseSitter;
using DTOs.Login;
using Google.Protobuf.WellKnownTypes;
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
        HouseOwnerDto? owner;
        HouseSitterDto? sitter;
        UserDto user = null;
        string password = null;

        if(_ownerRepo.GetAll().Where(o => o.Email == request.Email).Any(o => o.Password == request.Password))
        {
            owner = _ownerRepo.GetAll().SingleOrDefault(o => o.Email == request.Email);
            password = owner.Password;
            user = new UserDto
            {
                UserId = owner.UserId,
                Name = owner.Name,
                Email = owner.Email,
                ProfilePicture = owner.ProfilePicture,
                CPR = owner.CPR,
                Phone = owner.Phone,
                IsVerified = owner.IsVerified,
                Address = owner.Address,
                Biography = owner.Biography
            };
        }
        if(_sitterRepo.GetAll().Any(s => s.Email == request.Email) && _sitterRepo.GetAll().Any(s => s.Password == request.Password))
        {
            sitter = _sitterRepo.GetAll().SingleOrDefault(s => s.Email == request.Email);
            password = sitter.Password;
            user = new UserDto
            {
                UserId = sitter.UserId,
                Name = sitter.Name,
                Email = sitter.Email,
                ProfilePicture = sitter.ProfilePicture,
                CPR = sitter.CPR,
                Phone = sitter.Phone,
                IsVerified = sitter.IsVerified,
                Experience = sitter.Experience,
                Biography = sitter.Biography,
                Pictures = sitter.Pictures,
                Skills = sitter.Skills
            };
        }

        if (user != null || password.Equals(request.Password))
            return Ok(user);
        
        return Unauthorized("Invalid email or password.");
    }
}