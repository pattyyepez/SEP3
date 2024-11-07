package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseOwner;

import java.util.List;

public interface IHouseOwnerRepository
{
  List<HouseOwner> findAll();
  HouseOwner findById(int id);
  int save(HouseOwner houseOwner);
  void update(HouseOwner houseOwner);
  void deleteById(int id);
}
