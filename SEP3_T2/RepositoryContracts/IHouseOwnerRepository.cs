using DTOs.HouseOwner;

namespace RepositoryContracts;

public interface IHouseOwnerRepository
{
    Task<HouseOwnerDto> AddAsync(CreateHouseOwnerDto houseOwner);
    Task<HouseOwnerDto> UpdateAsync(int id, UpdateHouseOwnerDto houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwnerDto> GetSingleAsync(int id);
    IQueryable<HouseOwnerDto> GetAll();
}