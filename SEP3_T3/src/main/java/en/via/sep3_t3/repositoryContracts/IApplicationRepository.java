package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.Application;

import java.util.List;

public interface IApplicationRepository
{
  List<Application> findAll();
  Application findById(int listing_id, int sitter_id);
  void save(Application application);
  Application update(Application application);
  void deleteById(int listing_id, int sitter_id);
}
