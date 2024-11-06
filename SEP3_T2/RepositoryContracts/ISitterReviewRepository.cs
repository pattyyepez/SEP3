using DTOs.SitterReview;

namespace RepositoryContracts;

public interface ISitterReviewRepository
{
    Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview);
    Task DeleteAsync(int id);
    Task<SitterReviewDto> GetSingleAsync(int id);
    IQueryable<SitterReviewDto> GetAll();
}