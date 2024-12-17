package en.via.sep3_t3;

import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositoryContracts.IHouseListingRepository;
import en.via.sep3_t3.services.HouseListingServiceImpl;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HouseListingServiceImplTest
{

  private HouseListingServiceImpl service;
  private IHouseListingRepository repository;
  private StreamObserver<HouseListingResponse> responseObserver;

  @BeforeEach
  void setUp() {
    repository = mock(IHouseListingRepository.class);
    responseObserver = mock(StreamObserver.class);
    service = new HouseListingServiceImpl(repository);
  }

  @Test
  void testGet() {
    Date startDate = new Date(System.currentTimeMillis());
    Date endDate = new Date(System.currentTimeMillis() + 200000000);
    // Arrange
    HouseListingRequest request = HouseListingRequest.newBuilder()
        .setId(1)
        .build();

    HouseListing temp = new HouseListing();
    temp.setId(1);
    temp.setProfile_id(1);
    temp.setStatus("Open");
    temp.setStartDate(startDate);
    temp.setEndDate(endDate);

    when(repository.findById(1)).thenReturn(temp);

    ArgumentCaptor<HouseListingResponse> responseCaptor = ArgumentCaptor.forClass(HouseListingResponse.class);

    // Act
    service.getHouseListing(request, responseObserver);

    // Assert
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseListingResponse response = responseCaptor.getValue();
    assertEquals(1, response.getId());
    assertEquals(1, response.getProfileId());
    assertEquals("Open", response.getStatus());
    assertEquals(startDate, new Date(response.getStartDate()));
    assertEquals(endDate, new Date(response.getEndDate()));
  }

  @Test
  void testGetAll() {
    // Arrange
    AllHouseListingsRequest request = AllHouseListingsRequest.newBuilder().build();

    HouseListing temp1 = new HouseListing();
    temp1.setId(1);
    temp1.setProfile_id(1);
    temp1.setStatus("Open");
    temp1.setStartDate(new Date(System.currentTimeMillis()));
    temp1.setEndDate(new Date(System.currentTimeMillis() + 200000000));

    HouseListing temp2 = new HouseListing();
    temp2.setId(2);
    temp2.setProfile_id(2);
    temp2.setStatus("Closed");
    temp2.setStartDate(new Date(System.currentTimeMillis()));
    temp2.setEndDate(new Date(System.currentTimeMillis() + 200000000));

    List<HouseListing> temps = new ArrayList<>();
    temps.add(temp1);
    temps.add(temp2);

    when(repository.findAll()).thenReturn(temps);

    StreamObserver<AllHouseListingsResponse> allResponseObserver = mock(StreamObserver.class);
    ArgumentCaptor<AllHouseListingsResponse> responseCaptor = ArgumentCaptor.forClass(AllHouseListingsResponse.class);

    // Act
    service.getAllHouseListings(request, allResponseObserver);

    // Assert
    verify(allResponseObserver).onNext(responseCaptor.capture());
    verify(allResponseObserver).onCompleted();

    AllHouseListingsResponse response = responseCaptor.getValue();
    assertEquals(2, response.getHouseListingsCount());
  }

  @Test
  void testCreate() {
    // Arrange
    long now = System.currentTimeMillis();
    long then = now + 200000000;

    CreateHouseListingRequest request = CreateHouseListingRequest.newBuilder()
        .setProfileId(1)
        .setStartDate(now)
        .setEndDate(then)
        .build();

    HouseListing temp = new HouseListing();
    temp.setId(1);
    temp.setProfile_id(1);
    temp.setStatus("Open");
    temp.setStartDate(new Date(now));
    temp.setEndDate(new Date(then));

    when((repository).save(any(HouseListing.class))).thenReturn(1);
    ArgumentCaptor<HouseListingResponse> responseCaptor = ArgumentCaptor.forClass(HouseListingResponse.class);

    // Act
    service.createHouseListing(request, responseObserver);

    // Assert
    verify(repository).save(any(HouseListing.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseListingResponse response = responseCaptor.getValue();
    assertEquals(1, response.getId());
    assertEquals(1, response.getProfileId());
    assertEquals("Open", response.getStatus());
  }

  @Test
  void testUpdate() {
    // Arrange
    UpdateHouseListingRequest request = UpdateHouseListingRequest.newBuilder()
        .setId(1)
        .setStatus("Closed")
        .build();

    HouseListing temp = new HouseListing();
    temp.setId(1);
    temp.setProfile_id(1);
    temp.setStatus("Closed");
    temp.setStartDate(new Date(System.currentTimeMillis()));
    temp.setEndDate(new Date(System.currentTimeMillis() + 200000000));

    when(repository.update(any(HouseListing.class))).thenReturn(temp);

    ArgumentCaptor<HouseListingResponse> responseCaptor = ArgumentCaptor.forClass(HouseListingResponse.class);

    // Act
    service.updateHouseListing(request, responseObserver);

    // Assert
    verify(repository).update(any(HouseListing.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseListingResponse response = responseCaptor.getValue();
    assertEquals("Closed", response.getStatus());
  }

  @Test
  void testDelete() {
    // Arrange
    HouseListingRequest request = HouseListingRequest.newBuilder()
        .setId(1)
        .build();

    doNothing().when(repository).deleteById(1);
    ArgumentCaptor<HouseListingResponse> responseCaptor = ArgumentCaptor.forClass(HouseListingResponse.class);

    // Act
    service.deleteHouseListing(request, responseObserver);

    // Assert
    verify(repository).deleteById(1);
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();
  }
}

