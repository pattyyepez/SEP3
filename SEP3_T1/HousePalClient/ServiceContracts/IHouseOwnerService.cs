using DTOs.HouseOwner;

namespace Services;

public interface IHouseOwnerService {
    Task<HouseOwnerDto> AddAsync(CreateHouseOwnerDto houseOwner);
    Task<HouseOwnerDto> UpdateAsync(UpdateHouseOwnerDto houseOwner);
    Task DeleteAsync(int id);
    Task<HouseOwnerDto> GetSingleAsync(int id);
    IQueryable<HouseOwnerDto> GetAll();
}