package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.Application;
import en.via.sep3_t3.repositories.ApplicationRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ApplicationServiceImpl extends ApplicationServiceGrpc.ApplicationServiceImplBase {

  private final ApplicationRepository applicationRepository;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
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

  @Override
  public void createApplication(CreateApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
    try {
      Application application = new Application();
      application.setListing_id(request.getListingId());
      application.setSitter_id(request.getSitterId());
      application.setMessage(request.getMessage());
      application.setStatus(request.getStatus());
      application.setDate(parseDate(request.getDate()));

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
      application.setMessage(request.getMessage());
      application.setStatus(request.getStatus());
      application.setDate(parseDate(request.getDate()));

      applicationRepository.update(application);
      responseObserver.onNext(buildApplicationResponse(application));
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteApplication(DeleteApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
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
        .setDate(dateFormat.format(application.getDate()))
        .build();
  }

  private Date parseDate(String dateString) throws Exception {
    return dateFormat.parse(dateString);
  }
}
