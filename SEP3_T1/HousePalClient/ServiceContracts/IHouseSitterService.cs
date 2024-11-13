using DTOs.HouseSitter;

namespace Services;

public interface IHouseSitterService
{
    Task<HouseSitterDto> AddAsync(CreateHouseSitterDto houseSitter);
    Task<HouseSitterDto> UpdateAsync(UpdateHouseSitterDto houseSitter);
    Task DeleteAsync(int id);
    Task<HouseSitterDto> GetSingleAsync(int id);
    IQueryable<HouseSitterDto> GetAll();
    IQueryable<String> GetAllSkills();
}