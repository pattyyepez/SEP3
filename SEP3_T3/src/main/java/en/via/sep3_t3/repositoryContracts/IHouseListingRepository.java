package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseListing;

import java.util.List;

public interface IHouseListingRepository 
{
  List<HouseListing> findAll();
  HouseListing findById(int id);
  int save(HouseListing houseListing);
  HouseListing update(HouseListing houseListing);
  void deleteById(int id);
}
