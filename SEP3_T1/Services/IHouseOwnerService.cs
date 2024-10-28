using Entities;

namespace Services;

public interface IHouseOwnerService {
    Task<HouseOwner> AddAsync(HouseOwner houseOwner);
    Task<HouseOwner> UpdateAsync(HouseOwner houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwner> GetSingleAsync(int id);
    IQueryable<HouseOwner> GetAll();
}