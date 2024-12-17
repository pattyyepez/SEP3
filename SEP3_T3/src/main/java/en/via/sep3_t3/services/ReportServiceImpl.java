package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.Report;
import en.via.sep3_t3.repositoryContracts.IReportRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Report Service providing gRPC endpoints for report management.
 * <p>
 * This service allows clients to create, retrieve, update, and delete reports. It communicates with
 * the {@link IReportRepository} to handle persistence operations.
 * </p>
 */
@Service
public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {

  private final IReportRepository reportRepository;

  /**
   * Constructs a new ReportServiceImpl with the given repository.
   *
   * @param reportRepository the repository used for report data operations.
   */
  public ReportServiceImpl(IReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  /**
   * Retrieves a report by its ID.
   *
   * @param request the gRPC request containing the ID of the report to retrieve.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Retrieves all reports.
   *
   * @param request the gRPC request to fetch all reports.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getAllReports(AllReportsRequest request, StreamObserver<AllReportsResponse> responseObserver) {
    try {
      List<Report> reports = reportRepository.findAll();
      List<ReportResponse> reportResponses = new ArrayList<>();

      for (Report report : reports) {
        reportResponses.add(getReportResponse(report));
      }

      AllReportsResponse response = AllReportsResponse.newBuilder()
          .addAllReports(reportResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Creates a new report.
   *
   * @param request the gRPC request containing details of the report to create.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Updates the status of an existing report.
   *
   * @param request the gRPC request containing the report ID and the new status.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Deletes a report by its ID.
   *
   * @param request the gRPC request containing the ID of the report to delete.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Converts a {@link Report} entity to a {@link ReportResponse}.
   *
   * @param report the {@link Report} entity to convert.
   * @return a {@link ReportResponse} object containing the report data.
   */
  private static ReportResponse getReportResponse(Report report) {
    return ReportResponse.newBuilder()
        .setId(report.getId())
        .setReportingId(report.getReporting_id())
        .setReportedId(report.getReported_id())
        .setAdminId(report.getAdmin_id())
        .setComment(report.getComment())
        .setStatus(report.getStatus())
        .setDate(report.getDate() != null
            ? ZonedDateTime.of(report.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()
            : 0)
        .build();
  }
}
