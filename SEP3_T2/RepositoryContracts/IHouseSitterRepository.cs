using DTOs;
using DTOs.HouseOwner;
using DTOs.HouseSitter;

namespace RepositoryContracts;

public interface IHouseSitterRepository
{
    Task<HouseSitterDTO> AddAsync(CreateHouseSitterDTO houseSitter);
    Task<HouseSitterDTO> UpdateAsync(int id, UpdateHouseSitterDTO houseSitter);
    Task DeleteAsync(int id);
    Task<HouseSitterDTO> GetSingleAsync(int id);
    IQueryable<HouseSitterDTO> GetAll();
}