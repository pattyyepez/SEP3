using DTOs.HouseReview;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class HouseReviewRepository : IHouseReviewRepository
{
    private readonly HouseReviewService.HouseReviewServiceClient _client;

    public HouseReviewRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new HouseReviewService.HouseReviewServiceClient(channel);
    }


    public Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseReview)
    {
        HouseReviewResponse reply = _client.CreateHouseReview(new CreateHouseReviewRequest
        {
            ProfileId = houseReview.ProfileId,
            SitterId = houseReview.SitterId,
            Rating = houseReview.Rating,
            Comment = houseReview.Comment
        });
        
        return Task.FromResult(new HouseReviewDto
        {
            Id = reply.Id,
            ProfileId = reply.ProfileId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date).ToLocalTime(),
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteHouseReview(new HouseReviewRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    public Task<HouseReviewDto> GetSingleAsync(int id)
    {
        HouseReviewResponse reply = _client.GetHouseReview(new HouseReviewRequest()
        {
            Id = id
        });
        return Task.FromResult(new HouseReviewDto
        {
            Id = reply.Id,
            ProfileId = reply.ProfileId,
            SitterId = reply.SitterId,
            Rating = reply.Rating,
            Comment = reply.Comment,
            Date = new DateTime(1970, 1, 1).AddMilliseconds(reply.Date).ToLocalTime(),
        });
    }

    public IQueryable<HouseReviewDto> GetAll()
    {
        AllHouseReviewsResponse reply = _client.GetAllHouseReviews(new AllHouseReviewsRequest());
        var houseReviewResponses = reply.HouseReviews.ToList();
        var houseReviews = new List<HouseReviewDto>();

        foreach (var houseReview in houseReviewResponses)
        {
            houseReviews.Add(new HouseReviewDto
            {
                Id = houseReview.Id,
                ProfileId = houseReview.ProfileId,
                SitterId = houseReview.SitterId,
                Rating = houseReview.Rating,
                Comment = houseReview.Comment,
                Date = new DateTime(1970, 1, 1).AddMilliseconds(houseReview.Date).ToLocalTime(),
            });
        }

        return houseReviews.AsQueryable();
    }
}