package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseProfile;
import en.via.sep3_t3.repositoryContracts.IHouseProfileRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the House Profile Service that handles gRPC requests related to house profiles.
 * <p>
 * This service provides functionality for managing house profiles, including retrieving, creating,
 * updating, and deleting profiles, as well as fetching rules, chores, and amenities. It interacts
 * with the {@link IHouseProfileRepository} to perform operations on the underlying database.
 * </p>
 */
@Service
public class HouseProfileServiceImpl extends HouseProfileServiceGrpc.HouseProfileServiceImplBase {

  private final IHouseProfileRepository houseProfileRepository;

  /**
   * Constructs the HouseProfileServiceImpl with the provided repository.
   *
   * @param houseProfileRepository the repository used to interact with house profile data.
   */
  public HouseProfileServiceImpl(IHouseProfileRepository houseProfileRepository) {
    this.houseProfileRepository = houseProfileRepository;
  }

  /**
   * Handles the request to retrieve a house profile by its ID.
   *
   * @param request the gRPC request containing the ID of the house profile.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to retrieve all house profiles.
   *
   * @param request the gRPC request to fetch all house profiles.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getAllHouseProfiles(AllHouseProfilesRequest request, StreamObserver<AllHouseProfilesResponse> responseObserver) {
    try {
      List<HouseProfile> houseProfiles = houseProfileRepository.findAll();
      List<HouseProfileResponse> houseProfileResponses = new ArrayList<>();

      for (HouseProfile houseProfile : houseProfiles) {
        houseProfileResponses.add(getHouseProfileResponse(houseProfile));
      }

      AllHouseProfilesResponse response = AllHouseProfilesResponse.newBuilder()
          .addAllHouseProfiles(houseProfileResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to create a new house profile.
   *
   * @param request the gRPC request containing details of the house profile to create.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void createHouseProfile(CreateHouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      HouseProfile houseProfile = getHouseProfile(
          0, request.getOwnerId(),
          request.getTitle(), request.getDescription(),
          request.getAddress(), request.getRegion(),
          request.getCity(), request.getAmenitiesList(),
          request.getChoresList(), request.getRulesList(), request.getPicturesList());
      houseProfile.setId(houseProfileRepository.save(houseProfile));
      HouseProfileResponse response = getHouseProfileResponse(houseProfile);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to update an existing house profile.
   *
   * @param request the gRPC request containing details of the house profile to update.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void updateHouseProfile(UpdateHouseProfileRequest request, StreamObserver<HouseProfileResponse> responseObserver) {
    try {
      HouseProfile houseProfile = getHouseProfile(request.getId(), 0,
          request.getTitle(), request.getDescription(),
          request.getAddress(), request.getRegion(),
          request.getCity(), request.getAmenitiesList(),
          request.getChoresList(), request.getRulesList(), request.getPicturesList());
      houseProfile.setId(request.getId());
      houseProfile = houseProfileRepository.update(houseProfile);
      HouseProfileResponse response = getHouseProfileResponse(houseProfile);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to delete a house profile by its ID.
   *
   * @param request the gRPC request containing the ID of the house profile to delete.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to retrieve all rules.
   *
   * @param request the gRPC request to fetch all rules.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getAllRules(AllRulesRequest request, StreamObserver<AllRulesResponse> responseObserver) {
    try {
      List<String> rules = houseProfileRepository.findAllRules();
      AllRulesResponse response = AllRulesResponse.newBuilder().addAllRules(rules).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to retrieve all chores.
   *
   * @param request the gRPC request to fetch all chores.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getAllChores(AllChoresRequest request, StreamObserver<AllChoresResponse> responseObserver) {
    try {
      List<String> chores = houseProfileRepository.findAllChores();
      AllChoresResponse response = AllChoresResponse.newBuilder().addAllChores(chores).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to retrieve all amenities.
   *
   * @param request the gRPC request to fetch all amenities.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getAllAmenities(AllAmenitiesRequest request, StreamObserver<AllAmenitiesResponse> responseObserver) {
    try {
      List<String> amenities = houseProfileRepository.findAllAmenities();
      AllAmenitiesResponse response = AllAmenitiesResponse.newBuilder().addAllAmenities(amenities).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Helper method to create a {@link HouseProfile} object from the provided inputs.
   *
   * @param ProfileId the ID of the house profile.
   * @param ownerId the ID of the owner of the house profile.
   * @param title the title of the house profile.
   * @param description the description of the house profile.
   * @param address the address of the house profile.
   * @param region the region of the house profile.
   * @param city the city of the house profile.
   * @param amenities the list of amenities for the house profile.
   * @param chores the list of chores for the house profile.
   * @param rules the list of rules for the house profile.
   * @param pictures the list of pictures for the house profile.
   * @return a {@link HouseProfile} object populated with the provided values.
   */
  private static HouseProfile getHouseProfile(int ProfileId, int ownerId, String title, String description, String address,
      String region, String city, List<String> amenities,
      List<String> chores, List<String> rules, List<String> pictures) {
    HouseProfile houseProfile = new HouseProfile();
    houseProfile.setId(ProfileId);
    houseProfile.setOwner_id(ownerId);
    houseProfile.setTitle(title);
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

  /**
   * Helper method to convert a {@link HouseProfile} object to a {@link HouseProfileResponse}.
   *
   * @param houseProfile the {@link HouseProfile} object to convert.
   * @return a {@link HouseProfileResponse} object containing the house profile details.
   */
  private static HouseProfileResponse getHouseProfileResponse(HouseProfile houseProfile) {
    return HouseProfileResponse.newBuilder()
        .setId(houseProfile.getId())
        .setOwnerId(houseProfile.getOwner_id())
        .setTitle(houseProfile.getTitle())
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

