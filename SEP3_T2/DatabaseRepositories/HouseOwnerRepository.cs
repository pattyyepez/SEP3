using DTOs.HouseOwner;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseOwnerRepository : IHouseOwnerRepository
{

    private readonly HouseOwnerService.HouseOwnerServiceClient _client;

    public HouseOwnerRepository()
    {
            GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
            {  
                Credentials = ChannelCredentials.Insecure,
            });
            _client = new HouseOwnerService.HouseOwnerServiceClient(channel);
    }

    public Task<HouseOwnerDTO> AddAsync(CreateHouseOwnerDTO houseOwner)
    {
        HouseOwnerResponse reply = _client.CreateHouseOwner(new CreateHouseOwnerRequest
        {
            Email = houseOwner.Email,
            Password = houseOwner.Password,
            ProfilePicture = houseOwner.ProfilePicture,
            CPR = houseOwner.CPR,
            Phone = houseOwner.Phone,
            
            Address = houseOwner.Address,
            Biography = houseOwner.Biography
        });
        
        return Task.FromResult(new HouseOwnerDTO
        {
            UserId = reply.Id,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public Task<HouseOwnerDTO> UpdateAsync(int id, UpdateHouseOwnerDTO houseOwner)
    {
        HouseOwnerResponse reply = _client.UpdateHouseOwner(new UpdateHouseOwnerRequest()
        {
            Id = id,
            Email = houseOwner.Email,
            Password = houseOwner.Password,
            ProfilePicture = houseOwner.ProfilePicture,
            CPR = houseOwner.CPR,
            Phone = houseOwner.Phone,
            IsVerified = houseOwner.IsVerified,
            AdminId = houseOwner.AdminId,
            
            Address = houseOwner.Address,
            Biography = houseOwner.Biography
        });
        
        return Task.FromResult(new HouseOwnerDTO
        {
            UserId = reply.Id,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteHouseOwner(new HouseOwnerRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    // comment
    public Task<HouseOwnerDTO> GetSingleAsync(int id)
    {
        HouseOwnerResponse reply = _client.GetHouseOwner(new HouseOwnerRequest
        {
            Id = id
        });
        
        return Task.FromResult(new HouseOwnerDTO
        {
            UserId = reply.Id,
            Email = reply.Email,
            Password = reply.Password,
            ProfilePicture = reply.ProfilePicture,
            CPR = reply.CPR,
            Phone = reply.Phone,
            IsVerified = reply.IsVerified,
            AdminId = reply.AdminId,
            
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public IQueryable<HouseOwnerDTO> GetAll()
    {
        AllHouseOwnersResponse reply = _client.GetAllHouseOwners(new AllHouseOwnersRequest());
        var houseOwnerResponses = reply.HouseOwners.ToList();
        var houseOwners = new List<HouseOwnerDTO>();

        foreach (var houseOwner in houseOwnerResponses)
        {
            houseOwners.Add(new HouseOwnerDTO
            {
                UserId = houseOwner.Id,
                Email = houseOwner.Email,
                Password = houseOwner.Password,
                ProfilePicture = houseOwner.ProfilePicture,
                CPR = houseOwner.CPR,
                Phone = houseOwner.Phone,
                IsVerified = houseOwner.IsVerified,
                AdminId = houseOwner.AdminId,
            
                Address = houseOwner.Address,
                Biography = houseOwner.Biography
            });
        }

        return houseOwners.AsQueryable();
    }
}