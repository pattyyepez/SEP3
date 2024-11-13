using DTOs.SitterReview;

namespace Services;

public interface ISitterReviewService
{
    Task<SitterReviewDto> AddAsync(SitterReviewDto sitterReview);
    Task<SitterReviewDto> UpdateAsync(SitterReviewDto sitterReview);
    Task DeleteAsync(int id);
    Task<SitterReviewDto> GetSingleAsync(int id);
    IQueryable<SitterReviewDto> GetAll();
}