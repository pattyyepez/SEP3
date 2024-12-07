package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.HouseReview;

import java.util.List;

public interface IHouseReviewRepository
{
  List<HouseReview> findAll();
  HouseReview findById(int profileId, int sitterId);
  void save(HouseReview houseReview);
  HouseReview update(HouseReview houseReview);
  void deleteById(int profileId, int sitterId);
}
