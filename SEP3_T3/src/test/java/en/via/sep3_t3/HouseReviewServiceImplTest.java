package en.via.sep3_t3;

import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
import en.via.sep3_t3.services.HouseReviewServiceImpl;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HouseReviewServiceImplTest
{

  private HouseReviewServiceImpl service;
  private IHouseReviewRepository repository;
  private StreamObserver<HouseReviewResponse> responseObserver;
  private LocalDateTime date;

  @BeforeEach
  void setUp() {
    repository = mock(IHouseReviewRepository.class);
    responseObserver = mock(StreamObserver.class);
    service = new HouseReviewServiceImpl(repository);
    date = LocalDateTime.now();
  }

  @Test
  void testGet() {
    // Arrange
    HouseReviewRequest request = HouseReviewRequest.newBuilder()
        .setProfileId(1)
        .setSitterId(1)
        .build();

    HouseReview temp = new HouseReview();
    temp.setSitter_id(1);
    temp.setProfile_id(1);
    temp.setComment("sucked like a lot");
    temp.setRating(1);
    temp.setDate(date);

    when(repository.findById(1, 1)).thenReturn(temp);

    ArgumentCaptor<HouseReviewResponse> responseCaptor = ArgumentCaptor.forClass(HouseReviewResponse.class);

    // Act
    service.getHouseReview(request, responseObserver);

    // Assert
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseReviewResponse response = responseCaptor.getValue();
    assertEquals(1, response.getProfileId());
    assertEquals(1, response.getSitterId());
    assertEquals("sucked like a lot", response.getComment());
    assertEquals(1, response.getRating());
  }

  @Test
  void testGetAll() {
    // Arrange
    AllHouseReviewsRequest request = AllHouseReviewsRequest.newBuilder().build();

    HouseReview temp1 = new HouseReview();
    temp1.setSitter_id(1);
    temp1.setProfile_id(1);
    temp1.setComment("well it wasnt that bad but there were a lot of cockroaches");
    temp1.setRating(4);
    temp1.setDate(date);

    HouseReview temp2 = new HouseReview();
    temp2.setSitter_id(2);
    temp2.setProfile_id(2);
    temp2.setComment("i arrived and it was an empty lot with a tent");
    temp2.setRating(5);
    temp2.setDate(date);

    List<HouseReview> temps = new ArrayList<>();
    temps.add(temp1);
    temps.add(temp2);

    when(repository.findAll()).thenReturn(temps);

    StreamObserver<AllHouseReviewsResponse> allResponseObserver = mock(StreamObserver.class);
    ArgumentCaptor<AllHouseReviewsResponse> responseCaptor = ArgumentCaptor.forClass(AllHouseReviewsResponse.class);

    // Act
    service.getAllHouseReviews(request, allResponseObserver);

    // Assert
    verify(allResponseObserver).onNext(responseCaptor.capture());
    verify(allResponseObserver).onCompleted();

    AllHouseReviewsResponse response = responseCaptor.getValue();
    assertEquals(2, response.getHouseReviewsCount());
  }

  @Test
  void testCreate() {
    // Arrange
    CreateHouseReviewRequest request = CreateHouseReviewRequest.newBuilder()
        .setProfileId(1)
        .setSitterId(1)
        .setComment("there was a private chef with a rat in his hat")
        .setRating(3)
        .build();

    HouseReview temp = new HouseReview();
    temp.setSitter_id(1);
    temp.setProfile_id(1);
    temp.setComment("there was a private chef with a rat in his hat");
    temp.setRating(3);
    temp.setDate(date);

    doNothing().when(repository).save(any(HouseReview.class));
    ArgumentCaptor<HouseReviewResponse> responseCaptor = ArgumentCaptor.forClass(HouseReviewResponse.class);

    // Act
    service.createHouseReview(request, responseObserver);

    // Assert
    verify(repository).save(any(HouseReview.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseReviewResponse response = responseCaptor.getValue();
    assertEquals(1, response.getSitterId());
    assertEquals(1, response.getProfileId());
    assertEquals("there was a private chef with a rat in his hat", response.getComment());
    assertEquals(3, response.getRating());
  }

  @Test
  void testUpdate() {
    // Arrange
    UpdateHouseReviewRequest request = UpdateHouseReviewRequest.newBuilder()
        .setProfileId(1)
        .setSitterId(1)
        .setComment("it was significantly worse")
        .setRating(3)
        .build();

    HouseReview temp = new HouseReview();
    temp.setSitter_id(1);
    temp.setProfile_id(1);
    temp.setComment("it was significantly worse");
    temp.setRating(3);
    temp.setDate(date);

    when(repository.update(any(HouseReview.class))).thenReturn(temp);

    ArgumentCaptor<HouseReviewResponse> responseCaptor = ArgumentCaptor.forClass(HouseReviewResponse.class);

    // Act
    service.updateHouseReview(request, responseObserver);

    // Assert
    verify(repository).update(any(HouseReview.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseReviewResponse response = responseCaptor.getValue();
    assertEquals("it was significantly worse", response.getComment());
    assertEquals(3, response.getRating());
  }

  @Test
  void testDelete() {
    // Arrange
    HouseReviewRequest request = HouseReviewRequest.newBuilder()
        .setProfileId(1)
        .setSitterId(1)
        .build();

    doNothing().when(repository).deleteById(1, 1);
    ArgumentCaptor<HouseReviewResponse> responseCaptor = ArgumentCaptor.forClass(HouseReviewResponse.class);

    // Act
    service.deleteHouseReview(request, responseObserver);

    // Assert
    verify(repository).deleteById(1, 1);
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();
  }
}

