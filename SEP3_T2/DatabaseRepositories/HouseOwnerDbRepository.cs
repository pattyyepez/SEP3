using Entities;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseOwnerDbRepository : IHouseOwnerRepository
{

    private readonly HouseOwnerService.HouseOwnerServiceClient client;

    public HouseOwnerDbRepository()
    {
            GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
            {  
                Credentials = ChannelCredentials.Insecure,
            });
            client = new HouseOwnerService.HouseOwnerServiceClient(channel);
    }

    public Task<HouseOwner> AddAsync(HouseOwner houseOwner)
    {
        HouseOwnerResponse reply = client.CreateHouseOwner(new CreateHouseOwnerRequest
        {
            Address = houseOwner.Address, 
            Biography = houseOwner.Biography
        });
        
        return Task.FromResult(new HouseOwner
        {
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public Task<HouseOwner> UpdateAsync(HouseOwner houseOwner)
    {
        HouseOwnerResponse reply = client.UpdateHouseOwner(new UpdateHouseOwnerRequest()
        {
            OwnerId = houseOwner.OwnerId,
            Address = houseOwner.Address, 
            Biography = houseOwner.Biography
        });
        
        return Task.FromResult(new HouseOwner
        {
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public Task DeleteAsync(int id)
    {
        client.DeleteHouseOwner(new HouseOwnerRequest()
        {
            OwnerId = id
        });
        
        return Task.CompletedTask;
    }

    // comment
    public Task<HouseOwner> GetSingleAsync(int id)
    {
        HouseOwnerResponse reply = client.GetHouseOwner(new HouseOwnerRequest
        {
            OwnerId = id
        });
        
        return Task.FromResult(new HouseOwner
        {
            OwnerId = reply.OwnerId,
            Address = reply.Address,
            Biography = reply.Biography
        });
    }

    public IQueryable<HouseOwner> GetAll()
    {
        throw new NotImplementedException();
    }
}