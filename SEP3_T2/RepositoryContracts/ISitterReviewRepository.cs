using DTOs.SitterReview;

namespace RepositoryContracts;

public interface ISitterReviewRepository
{
    Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview);
    Task<SitterReviewDto> UpdateAsync(UpdateSitterReviewDto houseProfile);
    Task DeleteAsync(int ownerId, int sitterId);
    Task<SitterReviewDto> GetSingleAsync(int ownerId, int sitterId);
    IQueryable<SitterReviewDto> GetAll();
}