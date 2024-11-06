using DTOs.HouseReview;

namespace RepositoryContracts;

public interface IHouseReviewRepository
{
    Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseProfile);
    Task DeleteAsync(int id);
    Task<HouseReviewDto> GetSingleAsync(int id);
    IQueryable<HouseReviewDto> GetAll();
}