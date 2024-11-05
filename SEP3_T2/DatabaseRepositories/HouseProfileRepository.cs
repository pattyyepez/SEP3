using DTOs.HouseProfile;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseProfileRepository : IHouseProfileRepository
{
    private readonly HouseProfileService.HouseProfileServiceClient _client;

    public HouseProfileRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new HouseProfileService.HouseProfileServiceClient(channel);
    }
    
    public Task<HouseProfileDto> AddAsync(CreateHouseProfileDto houseProfile)
    {
        HouseProfileResponse reply = _client.CreateHouseProfile(new CreateHouseProfileRequest
        {
            Description = houseProfile.Description,
            City = houseProfile.City,
            OwnerId = houseProfile.OwnerId,
            Address = houseProfile.Address,
            Region = houseProfile.Region,
            
            Amenities = { houseProfile.Amenities },
            Chores = { houseProfile.Chores },
            Rules = { houseProfile.Rules },
            Pictures = { houseProfile.Pictures }
        });
        
        return Task.FromResult(new HouseProfileDto
        {
            Id = reply.Id,
            Description = reply.Description,
            City = reply.City,
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Region = reply.Region,
            
            Amenities = reply.Amenities.ToList(),
            Chores = reply.Chores.ToList(),
            Rules = reply.Rules.ToList(),
            Pictures = reply.Pictures.ToList()
        });
    }

    public Task<HouseProfileDto> UpdateAsync(int id, UpdateHouseProfileDto houseProfile)
    {
        HouseProfileResponse reply = _client.UpdateHouseProfile(new UpdateHouseProfileRequest()
        {
            Id = id,
            Description = houseProfile.Description,
            City = houseProfile.City,
            Address = houseProfile.Address,
            Region = houseProfile.Region,
            
            Amenities = { houseProfile.Amenities },
            Chores = { houseProfile.Chores },
            Rules = { houseProfile.Rules },
            Pictures = { houseProfile.Pictures }
        });
        
        return Task.FromResult(new HouseProfileDto
        {
            Id = reply.Id,
            Description = reply.Description,
            City = reply.City,
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Region = reply.Region,
            
            Amenities = reply.Amenities.ToList(),
            Chores = reply.Chores.ToList(),
            Rules = reply.Rules.ToList(),
            Pictures = reply.Pictures.ToList()
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteHouseProfile(new HouseProfileRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    public Task<HouseProfileDto> GetSingleAsync(int id)
    {
        HouseProfileResponse reply = _client.GetHouseProfile(new HouseProfileRequest()
        {
            Id = id
        });
        return Task.FromResult(new HouseProfileDto
        {
            Id = reply.Id,
            Description = reply.Description,
            City = reply.City,
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Region = reply.Region,
            
            Amenities = reply.Amenities.ToList(),
            Chores = reply.Chores.ToList(),
            Rules = reply.Rules.ToList(),
            Pictures = reply.Pictures.ToList()
        });
    }

    public IQueryable<HouseProfileDto> GetAll()
    {
        AllHouseProfilesResponse reply = _client.GetAllHouseProfiles(new AllHouseProfilesRequest());
        var houseProfileResponses = reply.HouseProfiles.ToList();
        var houseProfiles = new List<HouseProfileDto>();

        foreach (var houseProfile in houseProfileResponses)
        {
            houseProfiles.Add(new HouseProfileDto
            {
                Id = houseProfile.Id,
                Description = houseProfile.Description,
                City = houseProfile.City,
                OwnerId = houseProfile.OwnerId,
                Address = houseProfile.Address,
                Region = houseProfile.Region,
            
                Amenities = houseProfile.Amenities.ToList(),
                Chores = houseProfile.Chores.ToList(),
                Rules = houseProfile.Rules.ToList(),
                Pictures = houseProfile.Pictures.ToList()
            });
        }

        return houseProfiles.AsQueryable();
    }
}