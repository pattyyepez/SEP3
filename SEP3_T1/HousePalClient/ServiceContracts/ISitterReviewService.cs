using DTOs.SitterReview;

namespace HousePalClient.ServiceContracts;

public interface ISitterReviewService
{
    Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview);
    // Task<SitterReviewDto> UpdateAsync(UpdateSitt sitterReview);
    Task DeleteAsync(int id);
    Task<SitterReviewDto> GetSingleAsync(int id);
    IQueryable<SitterReviewDto> GetAll();
}