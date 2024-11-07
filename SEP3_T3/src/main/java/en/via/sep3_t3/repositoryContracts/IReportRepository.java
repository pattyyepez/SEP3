package en.via.sep3_t3.repositoryContracts;

import en.via.sep3_t3.domain.Report;

import java.util.List;

public interface IReportRepository
{
  List<Report> findAll();
  Report findById(int id);
  int save(Report report);
  void update(Report report);
  void deleteById(int id);
}
