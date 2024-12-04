using DTOs.HouseListing;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseListingRepository : IHouseListingRepository
{
    private readonly HouseListingService.HouseListingServiceClient _client;

    public HouseListingRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new HouseListingService.HouseListingServiceClient(channel);
    }


    public Task<HouseListingDto> AddAsync(CreateHouseListingDto houseListing)
    {
        HouseListingResponse reply = _client.CreateHouseListing(new CreateHouseListingRequest
        {
            ProfileId = houseListing.ProfileId,
            StartDate = new DateTimeOffset(houseListing.StartDate.ToDateTime(new TimeOnly())).ToUnixTimeMilliseconds(),
            EndDate = new DateTimeOffset(houseListing.EndDate.ToDateTime(new TimeOnly())).ToUnixTimeMilliseconds()
            // Status = houseListing.Status,
        });
        
        return Task.FromResult(new HouseListingDto
        {
            Id = reply.Id,
            ProfileId = reply.ProfileId,
            StartDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.StartDate).ToLocalTime()),
            EndDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.EndDate).ToLocalTime()),
            Status = reply.Status,
        });
    }

    public Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing)
    {
        HouseListingResponse reply = _client.UpdateHouseListing(new UpdateHouseListingRequest()
        {
            Id = houseListing.Id.Value,
            Status = houseListing.Status
        });
        
        return Task.FromResult(new HouseListingDto
        {
            Id = reply.Id,
            ProfileId = reply.ProfileId,
            StartDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.StartDate).ToLocalTime()),
            EndDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.EndDate).ToLocalTime()),
            Status = reply.Status,
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteHouseListing(new HouseListingRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    public Task<HouseListingDto> GetSingleAsync(int id)
    {
        HouseListingResponse reply = _client.GetHouseListing(new HouseListingRequest()
        {
            Id = id
        });
        return Task.FromResult(new HouseListingDto
        {
            Id = reply.Id,
            ProfileId = reply.ProfileId,
            StartDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.StartDate).ToLocalTime()),
            EndDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(reply.EndDate).ToLocalTime()),
            Status = reply.Status
        });
    }

    public IQueryable<HouseListingDto> GetAll()
    {
        AllHouseListingsResponse reply = _client.GetAllHouseListings(new AllHouseListingsRequest());
        var houseListingResponses = reply.HouseListings.ToList();
        var houseListings = new List<HouseListingDto>();

        foreach (var houseListing in houseListingResponses)
        {
            houseListings.Add(new HouseListingDto
            {
                Id = houseListing.Id,
                ProfileId = houseListing.ProfileId,
                StartDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(houseListing.StartDate).ToLocalTime()),
                EndDate = DateOnly.FromDateTime(new DateTime(1970, 1, 1).AddMilliseconds(houseListing.EndDate).ToLocalTime()),
                Status = houseListing.Status
            });
        }

        return houseListings.AsQueryable();
    }
}