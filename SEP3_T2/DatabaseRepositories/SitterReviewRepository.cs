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
            OwnerId = sitterReview.OwnerId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date).ToLocalTime(),
        });
    }
    
    public Task<SitterReviewDto> UpdateAsync(UpdateSitterReviewDto review)
    {
        var reply = _client.UpdateSitterReview(new UpdateSitterReviewRequest
        {
            OwnerId = review.OwnerId,
            SitterId = review.SitterId,
            Rating = review.Rating,
            Comment = review.Comment
        });
        
        return Task.FromResult(new SitterReviewDto()
        {
            OwnerId = reply.OwnerId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date).ToLocalTime()
        });
    }

    public Task DeleteAsync(int ownerId, int sitterId)
    {
        _client.DeleteSitterReview(new SitterReviewRequest()
        {
            OwnerId = ownerId,
            SitterId = sitterId
        });
        
        return Task.CompletedTask;
    }

    public Task<SitterReviewDto> GetSingleAsync(int ownerId, int sitterId)
    {
        SitterReviewResponse reply = _client.GetSitterReview(new SitterReviewRequest()
        {
            OwnerId = ownerId,
            SitterId = sitterId
        });
        return Task.FromResult(new SitterReviewDto
        {
            OwnerId = reply.OwnerId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date).ToLocalTime(),
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
                OwnerId = sitterReview.OwnerId,
                SitterId = sitterReview.SitterId,
                Rating = sitterReview.Rating,
                Comment = sitterReview.Comment,
                Date = new DateTime(1970, 1, 1).AddMilliseconds(sitterReview.Date).ToLocalTime(),
            });
        }

        return sitterReviews.AsQueryable();
    }
}