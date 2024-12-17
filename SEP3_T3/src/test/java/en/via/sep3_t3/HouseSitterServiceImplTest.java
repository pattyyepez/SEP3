package en.via.sep3_t3;

import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
import en.via.sep3_t3.services.HouseSitterServiceImpl;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HouseSitterServiceImplTest
{

  private HouseSitterServiceImpl service;
  private IHouseSitterRepository repository;
  private StreamObserver<HouseSitterResponse> responseObserver;

  @BeforeEach
  void setUp() {
    repository = mock(IHouseSitterRepository.class);
    responseObserver = mock(StreamObserver.class);
    service = new HouseSitterServiceImpl(repository);
  }

  @Test
  void testGet() {
    // Arrange
    HouseSitterRequest request = HouseSitterRequest.newBuilder()
        .setId(1)
        .build();

    HouseSitter temp = new HouseSitter();
    temp.setUserId(1);
    temp.setName("tester");
    temp.setPassword("12345");
    temp.setEmail("line.jensen@mail.com");
    temp.setCPR("12345678910123");
    temp.setPhone("12314123");
    temp.setProfilePicture("face.jpg");
    temp.setAdminId(1);
    temp.setVerified(true);
    temp.setBiography("i like sitting");
    temp.setExperience("ive been sitting all my life");
    temp.setSkills(List.of(new String[] {"sitting", "in", "houses"}));
    temp.setPictures(List.of(new String[] {"sitting.jpg", "in.png", "houses.gif"}));

    when(repository.findById(1)).thenReturn(temp);

    ArgumentCaptor<HouseSitterResponse> responseCaptor = ArgumentCaptor.forClass(HouseSitterResponse.class);

    // Act
    service.getHouseSitter(request, responseObserver);

    // Assert
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseSitterResponse response = responseCaptor.getValue();
    assertEquals(1, response.getId());
    assertEquals("12345", response.getPassword());
    assertEquals("line.jensen@mail.com", response.getEmail());
    assertEquals("12345678910123", response.getCPR());
    assertEquals("12314123", response.getPhone());
    assertEquals(1, response.getAdminId());
    assertTrue(response.getIsVerified());
    assertEquals("i like sitting", response.getBiography());
    assertEquals("ive been sitting all my life", response.getExperience());
  }

  @Test
  void testGetAll() {
    // Arrange
    AllHouseSittersRequest request = AllHouseSittersRequest.newBuilder().build();

    HouseSitter temp1 = new HouseSitter();
    temp1.setUserId(1);
    temp1.setName("tester");
    temp1.setPassword("12345");
    temp1.setEmail("line.jensen@mail.com");
    temp1.setCPR("12345678910123");
    temp1.setPhone("12314123");
    temp1.setProfilePicture("face.jpg");
    temp1.setAdminId(1);
    temp1.setVerified(true);
    temp1.setBiography("i like sitting");
    temp1.setExperience("ive been sitting all my life");
    temp1.setSkills(List.of(new String[] {"sitting", "in", "houses"}));
    temp1.setPictures(List.of(new String[] {"sitting.jpg", "in.png", "houses.gif"}));

    HouseSitter temp2 = new HouseSitter();
    temp2.setUserId(2);
    temp2.setName("tester");
    temp2.setPassword("12345");
    temp2.setEmail("line.jensen@mail.com");
    temp2.setCPR("12345678910123");
    temp2.setPhone("12314123");
    temp2.setProfilePicture("face.jpg");
    temp2.setAdminId(1);
    temp2.setVerified(true);
    temp2.setBiography("i like sitting");
    temp2.setExperience("ive been sitting all my life");
    temp2.setSkills(List.of(new String[] {"sitting", "in", "houses"}));
    temp2.setPictures(List.of(new String[] {"sitting.jpg", "in.png", "houses.gif"}));

    List<HouseSitter> temps = new ArrayList<>();
    temps.add(temp1);
    temps.add(temp2);

    when(repository.findAll()).thenReturn(temps);

    StreamObserver<AllHouseSittersResponse> allResponseObserver = mock(StreamObserver.class);
    ArgumentCaptor<AllHouseSittersResponse> responseCaptor = ArgumentCaptor.forClass(AllHouseSittersResponse.class);

    // Act
    service.getAllHouseSitters(request, allResponseObserver);

    // Assert
    verify(allResponseObserver).onNext(responseCaptor.capture());
    verify(allResponseObserver).onCompleted();

    AllHouseSittersResponse response = responseCaptor.getValue();
    assertEquals(2, response.getHouseSittersCount());
  }

  @Test
  void testCreate() {
    // Arrange
    CreateHouseSitterRequest request = CreateHouseSitterRequest.newBuilder()
        .setPassword("12345")
        .setEmail("line.jensen@mail.com")
        .setCPR("12345678910123")
        .setPhone("12314123")
        .setProfilePicture("face.jpg")
        .setBiography("i like sitting")
        .setExperience("ive been sitting all my life")
//        .setSkills(0, "sitting")
//        .setPictures(0, "sitter.jpg")
        .build();

    HouseSitter temp = new HouseSitter();
    temp.setUserId(1);
    temp.setPassword("12345");
    temp.setEmail("line.jensen@mail.com");
    temp.setCPR("12345678910123");
    temp.setPhone("12314123");
    temp.setProfilePicture("face.jpg");
    temp.setAdminId(1);
    temp.setVerified(true);
    temp.setBiography("i like sitting");
    temp.setExperience("ive been sitting all my life");
    temp.setSkills(List.of(new String[] {"sitting", "in", "houses"}));
    temp.setPictures(List.of(new String[] {"sitting.jpg", "in.png", "houses.gif"}));

    when((repository).save(any(HouseSitter.class))).thenReturn(1);
    ArgumentCaptor<HouseSitterResponse> responseCaptor = ArgumentCaptor.forClass(HouseSitterResponse.class);

    // Act
    service.createHouseSitter(request, responseObserver);

    // Assert
    verify(repository).save(any(HouseSitter.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseSitterResponse response = responseCaptor.getValue();
    assertEquals(1, response.getId());
    assertEquals("12345", response.getPassword());
    assertEquals("line.jensen@mail.com", response.getEmail());
    assertEquals("12345678910123", response.getCPR());
    assertEquals("12314123", response.getPhone());
//    assertEquals(1, response.getAdminId());
//    assertTrue(response.getIsVerified());
    assertEquals("i like sitting", response.getBiography());
    assertEquals("ive been sitting all my life", response.getExperience());
  }

  @Test
  void testUpdate() {
    // Arrange
    UpdateHouseSitterRequest request = UpdateHouseSitterRequest.newBuilder()
        .setCPR("12345678910123")
        .setPhone("12314123")
        .setProfilePicture("face.jpg")
        .setBiography("i like sitting")
        .setExperience("ive been sitting all my life+1")
//        .setSkills(0, "sitting")
//        .setPictures(0, "sitter.jpg")
        .build();

    HouseSitter temp = new HouseSitter();
    temp.setUserId(1);
    temp.setPassword("12345");
    temp.setEmail("line.jensen@mail.com");
    temp.setCPR("12345678910123");
    temp.setPhone("12314123");
    temp.setProfilePicture("face.jpg");
    temp.setAdminId(1);
    temp.setVerified(true);
    temp.setBiography("i like sitting");
    temp.setExperience("ive been sitting all my life+1");
    temp.setSkills(List.of(new String[] {"sitting", "in", "houses"}));
    temp.setPictures(List.of(new String[] {"sitting.jpg", "in.png", "houses.gif"}));

    doNothing().when(repository).update(any(HouseSitter.class));

    ArgumentCaptor<HouseSitterResponse> responseCaptor = ArgumentCaptor.forClass(HouseSitterResponse.class);

    // Act
    service.updateHouseSitter(request, responseObserver);

    // Assert
    verify(repository).update(any(HouseSitter.class));
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();

    HouseSitterResponse response = responseCaptor.getValue();
    assertEquals("ive been sitting all my life+1", response.getExperience());
  }

  @Test
  void testDelete() {
    // Arrange
    HouseSitterRequest request = HouseSitterRequest.newBuilder()
        .setId(1)
        .build();

    doNothing().when(repository).deleteById(1);
    ArgumentCaptor<HouseSitterResponse> responseCaptor = ArgumentCaptor.forClass(HouseSitterResponse.class);

    // Act
    service.deleteHouseSitter(request, responseObserver);

    // Assert
    verify(repository).deleteById(1);
    verify(responseObserver).onNext(responseCaptor.capture());
    verify(responseObserver).onCompleted();
  }
}

