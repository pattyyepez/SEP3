package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.repositoryContracts.IHouseOwnerRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseOwnerServiceImpl extends HouseOwnerServiceGrpc.HouseOwnerServiceImplBase {

  private final IHouseOwnerRepository houseOwnerRepository;

  public HouseOwnerServiceImpl(IHouseOwnerRepository houseOwnerRepository) {
    this.houseOwnerRepository = houseOwnerRepository;
  }

  @Override
  public void getHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = houseOwnerRepository.findById(request.getId());
      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getAllHouseOwners(AllHouseOwnersRequest request, StreamObserver<AllHouseOwnersResponse> responseObserver) {
    try {
      List<HouseOwner> houseOwners = houseOwnerRepository.findAll();
      List<HouseOwnerResponse> houseOwnerResponses = new ArrayList<>();

      for(HouseOwner houseOwner : houseOwners ) {
        houseOwnerResponses.add(getHouseOwnerResponse(houseOwner));
      }

      AllHouseOwnersResponse response = AllHouseOwnersResponse.newBuilder()
          .addAllHouseOwners(houseOwnerResponses)
          .build();

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
          false, 0, request.getAddress(),
          request.getBiography());
      houseOwner.setUserId(houseOwnerRepository.save(houseOwner));

      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      e.printStackTrace();
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
    return HouseOwnerResponse.newBuilder()
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
  }
}