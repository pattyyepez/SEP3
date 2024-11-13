package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseSitter;

import java.util.List;

public interface IHouseSitterRepository
{
  List<HouseSitter> findAll();
  List<String> findAllSkills();
  HouseSitter findById(int id);
  int save(HouseSitter houseSitter);
  void update(HouseSitter houseSitter);
  void deleteById(int id);
}
