package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.SitterReview;

import java.util.List;

public interface ISitterReviewRepository
{
  List<SitterReview> findAll();
  SitterReview findById(int id);
  int save(SitterReview sitterReview);
  void deleteById(int id);
}
