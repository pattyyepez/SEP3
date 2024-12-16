package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.Application;
import en.via.sep3_t3.repositoryContracts.IApplicationRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationServiceImpl extends ApplicationServiceGrpc.ApplicationServiceImplBase {

  private final IApplicationRepository applicationRepository;

  public ApplicationServiceImpl(IApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @Override
  public void getApplication(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
    try {
      Application application = applicationRepository.findById(request.getListingId(), request.getSitterId());
      ApplicationResponse response = buildApplicationResponse(application);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  public void getAllApplications(AllApplicationsRequest request, StreamObserver<AllApplicationsResponse> responseObserver) {
    try {
      List<Application> applications = applicationRepository.findAll();
      List<ApplicationResponse> applicationResponses = new ArrayList<>();

      for(Application application : applications ) {
        applicationResponses.add(buildApplicationResponse(application));
      }

      AllApplicationsResponse response = AllApplicationsResponse.newBuilder()
          .addAllApplications(applicationResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createApplication(CreateApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
    try {
      Application application = new Application();
      application.setListing_id(request.getListingId());
      application.setSitter_id(request.getSitterId());
      application.setMessage(request.getMessage());
      application.setStatus("Pending");
      application.setDate(LocalDateTime.now());

      applicationRepository.save(application);
      responseObserver.onNext(buildApplicationResponse(application));
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateApplication(UpdateApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
    try {
      Application application = new Application();
      application.setListing_id(request.getListingId());
      application.setSitter_id(request.getSitterId());
      application.setStatus(request.getStatus());

      application = applicationRepository.update(application);
      responseObserver.onNext(buildApplicationResponse(application));
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteApplication(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
    try {
      applicationRepository.deleteById(request.getListingId(), request.getSitterId());
      responseObserver.onNext(ApplicationResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private ApplicationResponse buildApplicationResponse(Application application) {
    return ApplicationResponse.newBuilder()
        .setListingId(application.getListing_id())
        .setSitterId(application.getSitter_id())
        .setMessage(application.getMessage())
        .setStatus(application.getStatus())
        .setDate(application.getDate() != null ?
            ZonedDateTime.of(application.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli() : 0)
        .build();
  }
}
