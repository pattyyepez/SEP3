package en.via.sep3_t3.server;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.*;
import en.via.sep3_t3.repositories.HouseProfileRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

      houseProfile.setOwner_id(houseProfileRepository.save(houseProfile));

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
      houseProfile.setOwner_id(request.getId());

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
      HouseProfileResponse response = HouseProfileResponse.newBuilder().build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private static HouseProfile getHouseProfile(int ownerId, String description, String address,
      String region, String city, List<String> amenities, List<String> chores, List<String> rules, List<String> pictures) {

    HouseProfile houseProfile = new HouseProfile();
    houseProfile.setOwner_id(ownerId);
    houseProfile.setDescription(description);
    houseProfile.setAddress(address);
    houseProfile.setRegion(region);
    houseProfile.setCity(city);
    houseProfile.setAmenities(convertStringsToAmenities(amenities));
    houseProfile.setChores(convertStringsToChores(chores));
    houseProfile.setRules(convertStringsToRules(rules));
    houseProfile.setPictures(convertStringsToHousePictures(pictures));

    return houseProfile;
  }
  private static ArrayList<Amenity> convertStringsToAmenities(List<String> amenities) {
    ArrayList<Amenity> amenityList = new ArrayList<>();
    for (String amenityName : amenities) {
      Amenity amenity = new Amenity();
      amenity.setType(amenityName);
      amenityList.add(amenity);
    }
    return amenityList;
  }

  private static ArrayList<Chore> convertStringsToChores(List<String> chores) {
    ArrayList<Chore> choreList = new ArrayList<>();
    for (String choreName : chores) {
      Chore chore = new Chore();
      chore.setType(choreName);
      choreList.add(chore);
    }
    return choreList;
  }

  private static ArrayList<Rule> convertStringsToRules(List<String> rules) {
    ArrayList<Rule> ruleList = new ArrayList<>();
    for (String ruleName : rules) {
      Rule rule = new Rule();
      rule.setType(ruleName);
      ruleList.add(rule);
    }
    return ruleList;
  }

  private static ArrayList<HousePicture> convertStringsToHousePictures(List<String> pictures) {
    ArrayList<HousePicture> pictureList = new ArrayList<>();
    for (String picturePath : pictures) {
      HousePicture picture = new HousePicture();
      picture.setPath(picturePath);
      pictureList.add(picture);
    }
    return pictureList;
  }

  private static HouseProfileResponse getHouseProfileResponse(HouseProfile houseProfile) {
    return HouseProfileResponse.newBuilder()
        .setId(houseProfile.getOwner_id())
        .setOwnerId(houseProfile.getOwner_id())
        .setDescription(houseProfile.getDescription())
        .setAddress(houseProfile.getAddress())
        .setRegion(houseProfile.getRegion())
        .setCity(houseProfile.getCity())
        .addAllAmenities(convertAmenitiesToStrings(houseProfile.getAmenities()))
        .addAllChores(convertChoresToStrings(houseProfile.getChores()))
        .addAllRules(convertRulesToStrings(houseProfile.getRules()))
        .addAllPictures(convertPicturesToStrings(houseProfile.getPictures()))
        .build();
  }
  private static List<String> convertAmenitiesToStrings(ArrayList<Amenity> amenities) {
    List<String> amenityNames = new ArrayList<>();
    for (Amenity amenity : amenities) {
      amenityNames.add(amenity.getType());
    }
    return amenityNames;
  }

  private static List<String> convertChoresToStrings(ArrayList<Chore> chores) {
    List<String> choreNames = new ArrayList<>();
    for (Chore chore : chores) {
      choreNames.add(chore.getType());
    }
    return choreNames;
  }

  private static List<String> convertRulesToStrings(ArrayList<Rule> rules) {
    List<String> ruleNames = new ArrayList<>();
    for (Rule rule : rules) {
      ruleNames.add(rule.getType());
    }
    return ruleNames;
  }

  private static List<String> convertPicturesToStrings(ArrayList<HousePicture> pictures) {
    List<String> picturePaths = new ArrayList<>();
    for (HousePicture picture : pictures) {
      picturePaths.add(picture.getPath());
    }
    return picturePaths;
  }
}
