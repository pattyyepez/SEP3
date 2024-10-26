using Entities;

namespace Services;

public interface IHouseOwnerService {
    Task<HouseOwner> GetHouseOwner(int id);
}