package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseProfile;

import java.util.List;

public interface IHouseProfileRepository
{
  List<HouseProfile> findAll();
  List<String> findAllRules();
  List<String> findAllChores();
  List<String> findAllAmenities();
  HouseProfile findById(int id);
  int save(HouseProfile houseProfile);
  HouseProfile update(HouseProfile houseProfile);
  void deleteById(int id);
}
