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

/**
 * Implementation of the Application Service which handles the gRPC requests related to applications.
 * <p>
 * This service provides functionality for managing job applications, including retrieving, creating,
 * updating, and deleting applications. It communicates with the {@link IApplicationRepository} to persist
 * and retrieve application data from the underlying database.
 * </p>
 */
@Service
public class ApplicationServiceImpl extends ApplicationServiceGrpc.ApplicationServiceImplBase {

  private final IApplicationRepository applicationRepository;

  /**
   * Constructs the ApplicationServiceImpl with the provided repository.
   *
   * @param applicationRepository the repository used to interact with the application data store.
   */
  public ApplicationServiceImpl(IApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  /**
   * Handles the request to get an application by its composite IDs: listing ID and sitter ID.
   * <p>
   * This method fetches a specific application based on the provided listing and sitter IDs and responds
   * with the corresponding {@link ApplicationResponse}. If the application is found, the response is
   * sent to the client. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the listing ID and sitter ID.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to get all applications.
   * <p>
   * This method retrieves all applications from the database and constructs a response containing a list
   * of {@link ApplicationResponse} objects. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request to get all applications.
   * @param responseObserver the observer used to send the response back to the client.
   */
  public void getAllApplications(AllApplicationsRequest request, StreamObserver<AllApplicationsResponse> responseObserver) {
    try {
      List<Application> applications = applicationRepository.findAll();
      List<ApplicationResponse> applicationResponses = new ArrayList<>();

      for (Application application : applications) {
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

  /**
   * Handles the request to create a new application.
   * <p>
   * This method creates a new application with the provided data, saves it to the database, and sends the
   * created application back in the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the application to be created.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to update an existing application.
   * <p>
   * This method updates the status of an existing application and sends the updated application back in
   * the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the application to be updated.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to delete an application by its composite IDs: listing ID and sitter ID.
   * <p>
   * This method deletes the specified application and responds with an empty {@link ApplicationResponse}.
   * If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the listing ID and sitter ID of the application to be deleted.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Constructs an {@link ApplicationResponse} object from the given {@link Application} entity.
   *
   * @param application the application entity to be converted to a response.
   * @return the constructed {@link ApplicationResponse}.
   */
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

