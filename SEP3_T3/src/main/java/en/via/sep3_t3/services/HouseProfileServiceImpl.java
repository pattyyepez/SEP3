package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseProfile;
import en.via.sep3_t3.repositories.HouseProfileRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseProfileServiceImpl extends HouseProfileServiceGrpc.HouseProfileServiceImplBase {

  private final HouseProfileRepository houseProfileRepository;

  public HouseProfileServiceImpl(HouseProfileRepository houseProfileRepository) {
    this.houseProfileRepository = houseProfileRepository;
  }

  @Override
  public void getHouseProfile(HouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      HouseProfile houseProfile = houseProfileRepository.findById(request.getId());
      HouseProfileResponse response = getHouseProfileResponse(houseProfile);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createHouseProfile(CreateHouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      HouseProfile houseProfile = getHouseProfile(request.getOwnerId(), request.getDescription(),
          request.getAddress(), request.getRegion(), request.getCity(), request.getAmenitiesList(),
          request.getChoresList(), request.getRulesList(), request.getPicturesList());
      houseProfile.setId(houseProfileRepository.save(houseProfile));
      HouseProfileResponse response = getHouseProfileResponse(houseProfile);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseProfile(UpdateHouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      HouseProfile houseProfile = getHouseProfile(request.getOwnerId(), request.getDescription(),
          request.getAddress(), request.getRegion(), request.getCity(), request.getAmenitiesList(),
          request.getChoresList(), request.getRulesList(), request.getPicturesList());
      houseProfile.setId(request.getId());
      houseProfileRepository.update(houseProfile);
      HouseProfileResponse response = getHouseProfileResponse(houseProfile);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteHouseProfile(HouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      houseProfileRepository.deleteById(request.getId());
      responseObserver.onNext(HouseProfileResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private static HouseProfile getHouseProfile(int ownerId, String description, String address,
      String region, String city, List<String> amenities,
      List<String> chores, List<String> rules, List<String> pictures) {
    HouseProfile houseProfile = new HouseProfile();
    houseProfile.setOwner_id(ownerId);
    houseProfile.setDescription(description);
    houseProfile.setAddress(address);
    houseProfile.setRegion(region);
    houseProfile.setCity(city);
    houseProfile.setAmenities(amenities);
    houseProfile.setChores(chores);
    houseProfile.setRules(rules);
    houseProfile.setPictures(pictures);
    return houseProfile;
  }

  private static HouseProfileResponse getHouseProfileResponse(HouseProfile houseProfile) {
    return HouseProfileResponse.newBuilder()
        .setId(houseProfile.getId())
        .setOwnerId(houseProfile.getOwner_id())
        .setDescription(houseProfile.getDescription())
        .setAddress(houseProfile.getAddress())
        .setRegion(houseProfile.getRegion())
        .setCity(houseProfile.getCity())
        .addAllAmenities(houseProfile.getAmenities())
        .addAllChores(houseProfile.getChores())
        .addAllRules(houseProfile.getRules())
        .addAllPictures(houseProfile.getPictures())
        .build();
  }
}
