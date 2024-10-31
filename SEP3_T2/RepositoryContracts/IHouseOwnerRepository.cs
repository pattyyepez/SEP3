using DTOs;
using DTOs.HouseOwner;

namespace RepositoryContracts;

public interface IHouseOwnerRepository
{
    Task<HouseOwnerDTO> AddAsync(CreateHouseOwnerDTO houseOwner);
    Task<HouseOwnerDTO> UpdateAsync(int id, UpdateHouseOwnerDTO houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwnerDTO> GetSingleAsync(int id);
    IQueryable<HouseOwnerDTO> GetAll();
}