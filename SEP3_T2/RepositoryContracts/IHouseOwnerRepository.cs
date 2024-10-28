using Entities;

namespace RepositoryContracts;

public interface IHouseOwnerRepository
{
    Task<HouseOwner> AddAsync(HouseOwner houseOwner);
    Task<HouseOwner> UpdateAsync(HouseOwner houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwner> GetSingleAsync(int id);
    IQueryable<HouseOwner> GetAll();
}