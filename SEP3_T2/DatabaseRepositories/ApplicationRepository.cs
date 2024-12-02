using DTOs.Application;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class ApplicationRepository : IApplicationRepository
{
    private readonly ApplicationService.ApplicationServiceClient _client;

    public ApplicationRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new ApplicationService.ApplicationServiceClient(channel);
    }


    public Task<ApplicationDto> AddAsync(CreateApplicationDto application)
    {
        ApplicationResponse reply = _client.CreateApplication(new CreateApplicationRequest
        {
            ListingId = application.ListingId,
            SitterId = application.SitterId,
            Message = application.Message
        });
        
        return Task.FromResult(new ApplicationDto
        {
            ListingId = reply.ListingId,
            SitterId = reply.SitterId,
            Message = reply.Message,
            Status = reply.Status,
            Date = new DateTime(1970, 1, 2).AddMilliseconds(reply.Date)
        });
    }

    public Task<ApplicationDto> UpdateAsync(int listingId, int sitterId, UpdateApplicationDto application)
    {
        ApplicationResponse reply = _client.UpdateApplication(new UpdateApplicationRequest()
        {
            ListingId = listingId,
            SitterId = sitterId,
            Status = application.Status
        });
        
        return Task.FromResult(new ApplicationDto
        {
            ListingId = reply.ListingId,
            SitterId = reply.SitterId,
            Message = reply.Message,
            Status = reply.Status,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date)
        });
    }

    public Task DeleteAsync(int listingId, int sitterId)
    {
        _client.DeleteApplication(new ApplicationRequest()
        {
            ListingId = listingId,
            SitterId = sitterId
        });
        
        return Task.CompletedTask;
    }

    public Task<ApplicationDto> GetSingleAsync(int listingId, int sitterId)
    {
        ApplicationResponse reply = _client.GetApplication(new ApplicationRequest()
        {
            ListingId = listingId,
            SitterId = sitterId
        });
        return Task.FromResult(new ApplicationDto
        {
            ListingId = reply.ListingId,
            SitterId = reply.SitterId,
            Status = reply.Status,
            Message = reply.Message,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date)
        });
    }

    public IQueryable<ApplicationDto> GetAll()
    {
        AllApplicationsResponse reply = _client.GetAllApplications(new AllApplicationsRequest());
        var applicationResponses = reply.Applications.ToList();
        var applications = new List<ApplicationDto>();

        foreach (var application in applicationResponses)
        {
            applications.Add(new ApplicationDto
            {
                ListingId = application.ListingId,
                SitterId = application.SitterId,
                Status = application.Status,
                Message = application.Message,
                Date = new DateTime(1970, 1, 1).AddMilliseconds(application.Date),
                
            });
        }

        return applications.AsQueryable();
    }
}