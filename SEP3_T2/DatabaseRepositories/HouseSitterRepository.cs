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

    public Task<HouseSitterDTO> AddAsync(CreateHouseSitterDTO houseSitter)
    {
        HouseSitterResponse reply = _client.CreateHouseSitter(new CreateHouseSitterRequest
        {
            Email = houseSitter.Email,
            Password = houseSitter.Password,
            ProfilePicture = houseSitter.ProfilePicture,
            CPR = houseSitter.CPR,
            Phone = houseSitter.Phone,
            
            Biography = houseSitter.Biography,
            Experience = houseSitter.Experience, 
            Pictures = houseSitter.Pictures.ToList(),
            Skills = houseSitter.Skills
        });
        
        return Task.FromResult(new HouseSitterDTO
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

    public Task<HouseSitterDTO> UpdateAsync(int id, UpdateHouseSitterDTO houseSitter)
    {
        HouseSitterResponse reply = _client.UpdateHouseSitter(new UpdateHouseSitterRequest()
        {
            Id = id,
            Email = houseSitter.Email,
            Password = houseSitter.Password,
            ProfilePicture = houseSitter.ProfilePicture,
            CPR = houseSitter.CPR,
            Phone = houseSitter.Phone,
            IsVerified = houseSitter.IsVerified,
            AdminId = houseSitter.AdminId,
            
            Address = houseSitter.Address,
            Biography = houseSitter.Biography
        });
        
        return Task.FromResult(new HouseSitterDTO
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
        _client.DeleteHouseSitter(new HouseSitterRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    // comment
    public Task<HouseSitterDTO> GetSingleAsync(int id)
    {
        HouseSitterResponse reply = _client.GetHouseSitter(new HouseSitterRequest
        {
            Id = id
        });
        
        return Task.FromResult(new HouseSitterDTO
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

    public IQueryable<HouseSitterDTO> GetAll()
    {
        throw new NotImplementedException();
    }
}