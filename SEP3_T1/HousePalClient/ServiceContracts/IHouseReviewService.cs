using DTOs.HouseReview;

namespace HousePalClient.ServiceContracts;

public interface IHouseReviewService
{
    Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseReview);
    Task<HouseReviewDto> UpdateAsync(UpdateHouseReviewDto houseReview);
    Task DeleteAsync(int profileId, int sitterId);
    Task<HouseReviewDto> GetSingleAsync(int profileId, int sitterId, bool includeProfile, bool includeSitter);
    IQueryable<HouseReviewDto> GetAllReviewsForProfile(int profileId);
    IQueryable<HouseReviewDto> GetAll();
}