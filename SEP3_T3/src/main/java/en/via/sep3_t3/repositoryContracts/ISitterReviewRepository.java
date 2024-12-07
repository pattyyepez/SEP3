package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.SitterReview;

import java.util.List;

public interface ISitterReviewRepository
{
  List<SitterReview> findAll();
  SitterReview findById(int ownerId, int sitterId);
  void save(SitterReview sitterReview);
  SitterReview update(SitterReview sitterReview);
  void deleteById(int ownerId, int sitterId);
}
