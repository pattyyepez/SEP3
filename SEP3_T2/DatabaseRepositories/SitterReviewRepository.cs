using DTOs.SitterReview;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class SitterReviewRepository : ISitterReviewRepository
{
    private readonly SitterReviewService.SitterReviewServiceClient _client;

    public SitterReviewRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new SitterReviewService.SitterReviewServiceClient(channel);
    }

    public Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview)
    {
        SitterReviewResponse reply = _client.CreateSitterReview(new CreateSitterReviewRequest
        {
            OwnerId = sitterReview.OwnerId,
            SitterId = sitterReview.SitterId,
            Rating = sitterReview.Rating,
            Comment = sitterReview.Comment
        });
        
        return Task.FromResult(new SitterReviewDto
        {
            Id = reply.Id,
            OwnerId = sitterReview.OwnerId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date)
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteSitterReview(new SitterReviewRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    public Task<SitterReviewDto> GetSingleAsync(int id)
    {
        SitterReviewResponse reply = _client.GetSitterReview(new SitterReviewRequest()
        {
            Id = id
        });
        return Task.FromResult(new SitterReviewDto
        {
            Id = reply.Id,
            OwnerId = reply.OwnerId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date)
        });
    }

    public IQueryable<SitterReviewDto> GetAll()
    {
        AllSitterReviewsResponse reply = _client.GetAllSitterReviews(new AllSitterReviewsRequest());
        var sitterReviewResponses = reply.SitterReviews.ToList();
        var sitterReviews = new List<SitterReviewDto>();

        foreach (var sitterReview in sitterReviewResponses)
        {
            sitterReviews.Add(new SitterReviewDto
            {
                Id = sitterReview.Id,
                OwnerId = sitterReview.OwnerId,
                SitterId = sitterReview.SitterId,
                Rating = sitterReview.Rating,
                Comment = sitterReview.Comment,
                Date = new DateTime(1970, 1, 1).AddMilliseconds(sitterReview.Date)
            });
        }

        return sitterReviews.AsQueryable();
    }
}