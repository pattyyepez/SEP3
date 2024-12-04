package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.Report;
import en.via.sep3_t3.repositoryContracts.IReportRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {

  private final IReportRepository reportRepository;

  public ReportServiceImpl(IReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  @Override
  public void getReport(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      Report report = reportRepository.findById(request.getId());
      ReportResponse response = getReportResponse(report);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getAllReports(AllReportsRequest request,
      StreamObserver<AllReportsResponse> responseObserver)
  {
    try
    {
      List<Report> reports = reportRepository.findAll();
      List<ReportResponse> reportResponses = new ArrayList<>();

      for (Report report : reports)
      {
        reportResponses.add(getReportResponse(report));
      }

      AllReportsResponse response = AllReportsResponse.newBuilder()
          .addAllReports(reportResponses).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createReport(CreateReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      Report report = new Report();
      report.setReporting_id(request.getReportingId());
      report.setReported_id(request.getReportedId());
      report.setAdmin_id(request.getAdminId());
      report.setComment(request.getComment());
      report.setStatus("Pending");
      report.setDate(LocalDateTime.now());

      int id = reportRepository.save(report);
      report.setId(id);
      ReportResponse response = getReportResponse(report);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateReport(UpdateReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      Report report = reportRepository.findById(request.getId());
      report.setStatus(request.getStatus());

      reportRepository.update(report);
      ReportResponse response = getReportResponse(report);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteReport(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      reportRepository.deleteById(request.getId());
      ReportResponse response = ReportResponse.newBuilder().build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private static ReportResponse getReportResponse(Report report) {
    return ReportResponse.newBuilder()
        .setId(report.getId())
        .setReportingId(report.getReporting_id())
        .setReportedId(report.getReported_id())
        .setAdminId(report.getAdmin_id())
        .setComment(report.getComment())
        .setStatus(report.getStatus())
        .setDate(report.getDate() != null ? ZonedDateTime.of(report.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli() : 0)
        .build();
  }
}
