using DTOs.HouseOwner;

namespace Services;

public interface IHouseOwnerService {
    Task<HouseOwnerDto> AddAsync(HouseOwnerDto houseOwner);
    Task<HouseOwnerDto> UpdateAsync(HouseOwnerDto houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwnerDto> GetSingleAsync(int id);
    IQueryable<HouseOwnerDto> GetAll();
}