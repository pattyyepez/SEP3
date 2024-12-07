using DTOs.HouseReview;

namespace RepositoryContracts;

public interface IHouseReviewRepository
{
    Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseProfile);
    Task<HouseReviewDto> UpdateAsync(UpdateHouseReviewDto houseProfile);
    Task DeleteAsync(int profileId, int sitterId);
    Task<HouseReviewDto> GetSingleAsync(int profileId, int sitterId);
    IQueryable<HouseReviewDto> GetAll();
}