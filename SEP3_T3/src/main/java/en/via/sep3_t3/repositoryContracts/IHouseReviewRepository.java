package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseReview;

import java.util.List;

public interface IHouseReviewRepository
{
  List<HouseReview> findAll();
  HouseReview findById(int id);
  int save(HouseReview houseReview);
  void deleteById(int id);
}
