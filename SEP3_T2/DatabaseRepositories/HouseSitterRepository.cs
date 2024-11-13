using DTOs.HouseSitter;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseSitterRepository : IHouseSitterRepository
{

    private readonly HouseSitterService.HouseSitterServiceClient _client;

    public HouseSitterRepository()
    {
            GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
            {  
                Credentials = ChannelCredentials.Insecure,
            });
            _client = new HouseSitterService.HouseSitterServiceClient(channel);
    }

    public Task<HouseSitterDto> AddAsync(CreateHouseSitterDto houseSitter)
    {
        HouseSitterResponse reply = _client.CreateHouseSitter(new CreateHouseSitterRequest
        {
            Name = houseSitter.Name,
            Email = houseSitter.Email,
            Password = houseSitter.Password,
            ProfilePicture = houseSitter.ProfilePicture,
            CPR = houseSitter.CPR,
            Phone = houseSitter.Phone,
            Pictures = { houseSitter.Pictures },
            Skills = { houseSitter.Skills },
            
            Biography = houseSitter.Biography,
            Experience = houseSitter.Experience
        });
        
        return Task.FromResult(new HouseSitterDto
        {
            UserId = reply.Id,
            Name = reply.Name,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Biography = reply.Biography,
            Experience = reply.Experience, 
            Pictures = reply.Pictures.ToList(),
            Skills = reply.Skills.ToList()
        });
    }

    public Task<HouseSitterDto> UpdateAsync(int id, UpdateHouseSitterDto houseSitter)
    {
        HouseSitterResponse reply = _client.UpdateHouseSitter(new UpdateHouseSitterRequest()
        {
            Id = id,
            Name = houseSitter.Name,
            Email = houseSitter.Email,
            Password = houseSitter.Password,
            ProfilePicture = houseSitter.ProfilePicture,
            CPR = houseSitter.CPR,
            Phone = houseSitter.Phone,
            IsVerified = houseSitter.IsVerified,
            AdminId = houseSitter.AdminId,
            
            Biography = houseSitter.Biography,
            Experience = houseSitter.Experience, 
            Pictures = { houseSitter.Pictures },
            Skills = { houseSitter.Skills }
        });
        
        return Task.FromResult(new HouseSitterDto
        {
            UserId = reply.Id,
            
            Name = reply.Name,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Biography = reply.Biography,
            Experience = reply.Experience, 
            Pictures = reply.Pictures.ToList(),
            Skills = reply.Skills.ToList()
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteHouseSitter(new HouseSitterRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    // comment
    public Task<HouseSitterDto> GetSingleAsync(int id)
    {
        HouseSitterResponse reply = _client.GetHouseSitter(new HouseSitterRequest
        {
            Id = id
        });
        return Task.FromResult(new HouseSitterDto
        {
            UserId = reply.Id,
            
            Name = reply.Name,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Biography = reply.Biography,
            Experience = reply.Experience, 
            Pictures = reply.Pictures.ToList(),
            Skills = reply.Skills.ToList()
        });
    }

    public IQueryable<HouseSitterDto> GetAll()
    {
        AllHouseSittersResponse reply = _client.GetAllHouseSitters(new AllHouseSittersRequest());
        var houseSitterResponses = reply.HouseSitters.ToList();
        var houseSitters = new List<HouseSitterDto>();

        foreach (var houseSitter in houseSitterResponses)
        {
            houseSitters.Add(new HouseSitterDto
            {
                UserId = houseSitter.Id,
            
                Name = houseSitter.Name,
                Email = houseSitter.Email,
                Password = houseSitter.Password,
                ProfilePicture = houseSitter.ProfilePicture,
                CPR = houseSitter.CPR,
                Phone = houseSitter.Phone,
                IsVerified = houseSitter.IsVerified,
                AdminId = houseSitter.AdminId,
            
                Biography = houseSitter.Biography,
                Experience = houseSitter.Experience, 
                Pictures = houseSitter.Pictures.ToList(),
                Skills = houseSitter.Skills.ToList()
            });
        }

        return houseSitters.AsQueryable();
    }

    public IQueryable<String> GetAllSkills()
    {
        return _client.GetAllSkills(new AllSkillsRequest()).Skill.AsQueryable();
    }
}