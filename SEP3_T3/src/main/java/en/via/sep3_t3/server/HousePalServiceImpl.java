package en.via.sep3_t3.server;

import en.via.sep3_t3.CreateHouseOwnerRequest;
import en.via.sep3_t3.HouseOwnerRequest;
import en.via.sep3_t3.HouseOwnerResponse;
import en.via.sep3_t3.HouseOwnerServiceGrpc;
import en.via.sep3_t3.UpdateHouseOwnerRequest;
import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.repositories.HouseOwnerRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class HousePalServiceImpl extends HouseOwnerServiceGrpc.HouseOwnerServiceImplBase {

  private final HouseOwnerRepository houseOwnerRepository;

  public HousePalServiceImpl(HouseOwnerRepository houseOwnerRepository) {
    this.houseOwnerRepository = houseOwnerRepository;
  }

  @Override
  public void getHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = houseOwnerRepository.findById(request.getId() );
      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createHouseOwner(CreateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = getHouseOwner(request.getEmail(), request.getPassword(),
          request.getProfilePicture(), request.getCPR(), request.getPhone(),
          request.getIsVerified(), request.getAdminId(), request.getAddress(),
          request.getBiography());
      houseOwner.setUserId(houseOwnerRepository.save(houseOwner));

      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseOwner(UpdateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = getHouseOwner(request.getEmail(), request.getPassword(),
          request.getProfilePicture(), request.getCPR(), request.getPhone(),
          request.getIsVerified(), request.getAdminId(), request.getAddress(),
          request.getBiography());
      houseOwner.setUserId(request.getId());

      houseOwnerRepository.update(houseOwner);
      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      houseOwnerRepository.deleteById(request.getId());

      HouseOwnerResponse response = HouseOwnerResponse.newBuilder().build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private static HouseOwner getHouseOwner(String email,
      String password, String profilePicture, String cpr, String phone,
      boolean isVerified, int adminId, String address, String biography)
  {
    HouseOwner houseOwner = new HouseOwner();
    houseOwner.setEmail(email);
    houseOwner.setPassword(password);
    houseOwner.setProfilePicture(profilePicture);
    houseOwner.setCPR(cpr);
    houseOwner.setPhone(phone);
    houseOwner.setVerified(isVerified);
    houseOwner.setAdminId(adminId);
    houseOwner.setAddress(address);
    houseOwner.setBiography(biography);
    return houseOwner;
  }

  private static HouseOwnerResponse getHouseOwnerResponse(HouseOwner houseOwner)
  {
    HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
        .setId(houseOwner.getUserId())
        .setEmail(houseOwner.getEmail())
        .setPassword(houseOwner.getPassword())
        .setProfilePicture(houseOwner.getProfilePicture())
        .setCPR(houseOwner.getCPR())
        .setPhone(houseOwner.getPhone())
        .setIsVerified(houseOwner.isVerified())
        .setAdminId(houseOwner.getAdminId())
        .setAddress(houseOwner.getAddress())
        .setBiography(houseOwner.getBiography())
        .build();
    return response;
  }
}
