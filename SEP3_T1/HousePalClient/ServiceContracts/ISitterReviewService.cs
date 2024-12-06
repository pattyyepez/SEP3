using DTOs.SitterReview;

namespace HousePalClient.ServiceContracts;

public interface ISitterReviewService
{
    Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview);
    // Task<SitterReviewDto> UpdateAsync(UpdateSitt sitterReview);
    Task DeleteAsync(int id);
    Task<SitterReviewDto> GetSingleAsync(int id, bool includeOwner, bool includeSitter);
    IQueryable<SitterReviewDto> GetAll(bool includeOwner, bool includeSitter);
    IQueryable<SitterReviewDto> GetAllReviewsForSitter(int sitterId);
}