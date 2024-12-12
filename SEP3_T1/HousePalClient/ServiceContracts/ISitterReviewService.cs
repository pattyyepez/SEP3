using DTOs.SitterReview;

namespace HousePalClient.ServiceContracts;

public interface ISitterReviewService
{
    Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview);
    Task<SitterReviewDto> UpdateAsync(UpdateSitterReviewDto sitterReview);
    Task DeleteAsync(int ownerId, int sitterId);
    Task<SitterReviewDto> GetSingleAsync(int ownerId, int sitterId, bool includeOwner, bool includeSitter);
    IQueryable<SitterReviewDto> GetAll(bool includeOwner, bool includeSitter);
    IQueryable<SitterReviewDto> GetAllReviewsForSitter(int sitterId);
    double GetAverageForSitter(int sitterId);
}