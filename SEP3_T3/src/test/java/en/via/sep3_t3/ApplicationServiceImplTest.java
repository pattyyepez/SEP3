package en.via.sep3_t3;

import en.via.sep3_t3.domain.Application;
import en.via.sep3_t3.repositoryContracts.IApplicationRepository;
import en.via.sep3_t3.services.ApplicationServiceImpl;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

  private ApplicationServiceImpl service;
  private IApplicationRepository applicationRepository;
  private StreamObserver<ApplicationResponse> responseObserver;

  @BeforeEach
  void setUp() {
    applicationRepository = mock(IApplicationRepository.class);
    responseObserver = mock(StreamObserver.class);
    service = new ApplicationServiceImpl(applicationRepository);
  }

  @Test
  void testGetApplication() {
    // Arrange
    ApplicationRequest request = ApplicationRequest.newBuilder()
        .setListingId(1)
        .setSitterId(2)
        .build();

    Application application = new Application();
    application.setListing_id(1);
    application.setSitter_id(2);
    application.setMessage("Test message");
    application.setStatus("Pending");
    application.setDate(LocalDateTime.now());

    when(applicationRepository.findById(1, 2)).thenReturn(application);

    ArgumentCaptor<ApplicationResponse> responseCaptor = ArgumentCaptor.forClass(ApplicationResponse.class);

    // Act
    service.getApplication(request, responseObserver);

    // Assert
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    ApplicationResponse response = responseCaptor.getValue();
    assertEquals(1, response.getListingId());
    assertEquals(2, response.getSitterId());
    assertEquals("Test message", response.getMessage());
    assertEquals("Pending", response.getStatus());
  }

  @Test
  void testGetAllApplications() {
    // Arrange
    AllApplicationsRequest request = AllApplicationsRequest.newBuilder().build();

    Application application1 = new Application();
    application1.setListing_id(1);
    application1.setSitter_id(2);
    application1.setMessage("Message 1");
    application1.setStatus("Pending");
    application1.setDate(LocalDateTime.now());

    Application application2 = new Application();
    application2.setListing_id(3);
    application2.setSitter_id(4);
    application2.setMessage("Message 2");
    application2.setStatus("Approved");
    application2.setDate(LocalDateTime.now());

    List<Application> applications = new ArrayList<>();
    applications.add(application1);
    applications.add(application2);

    when(applicationRepository.findAll()).thenReturn(applications);

    StreamObserver<AllApplicationsResponse> allResponseObserver = mock(StreamObserver.class);
    ArgumentCaptor<AllApplicationsResponse> responseCaptor = ArgumentCaptor.forClass(AllApplicationsResponse.class);

    // Act
    service.getAllApplications(request, allResponseObserver);

    // Assert
    verify(allResponseObserver).onNext(responseCaptor.capture());
    verify(allResponseObserver).onCompleted();

    AllApplicationsResponse response = responseCaptor.getValue();
    assertEquals(2, response.getApplicationsCount());
  }

  @Test
  void testCreateApplication() {
    // Arrange
    CreateApplicationRequest request = CreateApplicationRequest.newBuilder()
        .setListingId(1)
        .setSitterId(2)
        .setMessage("Test create message")
        .build();

    Application application = new Application();
    application.setListing_id(1);
    application.setSitter_id(2);
    application.setMessage("Test create message");
    application.setStatus("Pending");
    application.setDate(LocalDateTime.now());

    doNothing().when(applicationRepository).save(any(Application.class));
    ArgumentCaptor<ApplicationResponse> responseCaptor = ArgumentCaptor.forClass(ApplicationResponse.class);

    // Act
    service.createApplication(request, responseObserver);

    // Assert
    verify(applicationRepository).save(any(Application.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    ApplicationResponse response = responseCaptor.getValue();
    assertEquals(1, response.getListingId());
    assertEquals(2, response.getSitterId());
    assertEquals("Test create message", response.getMessage());
  }

  @Test
  void testUpdateApplication() {
    // Arrange
    UpdateApplicationRequest request = UpdateApplicationRequest.newBuilder()
        .setListingId(1)
        .setSitterId(2)
        .setStatus("Approved")
        .build();

    Application updatedApplication = new Application();
    updatedApplication.setListing_id(1);
    updatedApplication.setSitter_id(2);
    updatedApplication.setStatus("Approved");
    updatedApplication.setMessage("Test message");
    updatedApplication.setDate(LocalDateTime.now());

    when(applicationRepository.update(any(Application.class))).thenReturn(updatedApplication);

    ArgumentCaptor<ApplicationResponse> responseCaptor = ArgumentCaptor.forClass(ApplicationResponse.class);

    // Act
    service.updateApplication(request, responseObserver);

    // Assert
    verify(applicationRepository).update(any(Application.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    ApplicationResponse response = responseCaptor.getValue();
    assertEquals("Approved", response.getStatus());
  }

  @Test
  void testDeleteApplication() {
    // Arrange
    ApplicationRequest request = ApplicationRequest.newBuilder()
        .setListingId(1)
        .setSitterId(2)
        .build();

    doNothing().when(applicationRepository).deleteById(1, 2);
    ArgumentCaptor<ApplicationResponse> responseCaptor = ArgumentCaptor.forClass(ApplicationResponse.class);

    // Act
    service.deleteApplication(request, responseObserver);

    // Assert
    verify(applicationRepository).deleteById(1, 2);
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();
  }
}

