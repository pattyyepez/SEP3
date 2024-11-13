using DTOs.HouseSitter;

namespace RepositoryContracts;

public interface IHouseSitterRepository
{
    Task<HouseSitterDto> AddAsync(CreateHouseSitterDto houseSitter);
    Task<HouseSitterDto> UpdateAsync(int id, UpdateHouseSitterDto houseSitter);
    Task DeleteAsync(int id);
    Task<HouseSitterDto> GetSingleAsync(int id);
    IQueryable<HouseSitterDto> GetAll();
    IQueryable<String> GetAllSkills();
}