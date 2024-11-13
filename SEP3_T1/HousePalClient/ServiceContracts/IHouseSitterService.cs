using DTOs.HouseSitter;

namespace Services;

public interface IHouseSitterService
{
    Task<HouseSitterDto> AddAsync(HouseSitterDto houseSitter);
    Task<HouseSitterDto> UpdateAsync(HouseSitterDto houseSitter);
    Task DeleteAsync(int id);
    Task<HouseSitterDto> GetSingleAsync(int id);
    IQueryable<HouseSitterDto> GetAll();
}