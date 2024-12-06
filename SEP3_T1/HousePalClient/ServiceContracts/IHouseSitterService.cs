using DTOs.HouseSitter;

namespace HousePalClient.ServiceContracts;

public interface IHouseSitterService
{
    Task<HouseSitterDto> AddAsync(CreateHouseSitterDto houseSitter);
    Task<HouseSitterDto> UpdateAsync(UpdateHouseSitterDto houseSitter);
    Task DeleteAsync(int id);
    Task<HouseSitterDto> GetSingleAsync(int id, bool includeReviews);
    IQueryable<HouseSitterDto> GetAll();
    IQueryable<String> GetAllSkills();
}