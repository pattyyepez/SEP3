package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.Reports;
import en.via.sep3_t3.repositories.ReportRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {

  private final ReportRepository reportRepository;

  public ReportServiceImpl(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  @Override
  public void getReport(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      Reports report = reportRepository.findById(request.getId());
      ReportResponse response = getReportResponse(report);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createReport(CreateReportRequest request, StreamObserver<ReportResponse> responseObserver) {
    try {
      Reports report = new Reports();
      report.setOwner_id(request.getOwnerId());
      report.setSitter_id(request.getSitterId());
      report.setAdmin_id(request.getAdminId());
      report.setComment(request.getComment());
      report.setStatus(request.getStatus());
      report.setDate(java.sql.Date.valueOf(request.getDate()));

      reportRepository.save(report);
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
      Reports report = reportRepository.findById(request.getId());
      report.setComment(request.getComment());
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

  private static ReportResponse getReportResponse(Reports report) {
    return ReportResponse.newBuilder()
        .setId(report.getId())
        .setOwnerId(report.getOwner_id())
        .setSitterId(report.getSitter_id())
        .setAdminId(report.getAdmin_id())
        .setComment(report.getComment())
        .setStatus(report.getStatus())
        .setDate(report.getDate().toString())
        .build();
  }
}
