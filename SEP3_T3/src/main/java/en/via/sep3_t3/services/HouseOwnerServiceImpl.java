package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.repositoryContracts.IHouseOwnerRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the House Owner Service that handles gRPC requests related to house owners.
 * <p>
 * This service provides functionality for managing house owners, including retrieving, creating,
 * updating, and deleting house owners. It interacts with the {@link IHouseOwnerRepository} to persist
 * and retrieve house owner data from the underlying database.
 * </p>
 */
@Service
public class HouseOwnerServiceImpl extends HouseOwnerServiceGrpc.HouseOwnerServiceImplBase {

  private final IHouseOwnerRepository houseOwnerRepository;

  /**
   * Constructs the HouseOwnerServiceImpl with the provided repository.
   *
   * @param houseOwnerRepository the repository used to interact with the house owner data store.
   */
  public HouseOwnerServiceImpl(IHouseOwnerRepository houseOwnerRepository) {
    this.houseOwnerRepository = houseOwnerRepository;
  }

  /**
   * Handles the request to get a house owner by their ID.
   * <p>
   * This method retrieves a specific house owner based on the provided ID and responds
   * with the corresponding {@link HouseOwnerResponse}. If the owner is found, the response is
   * sent to the client. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the ID of the house owner.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to get all house owners.
   * <p>
   * This method retrieves all house owners from the database and constructs a response containing a list
   * of {@link HouseOwnerResponse} objects. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request to get all house owners.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Handles the request to create a new house owner.
   * <p>
   * This method creates a new house owner with the provided data, saves it to the database, and sends the
   * created house owner back in the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the house owner to be created.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void createHouseOwner(CreateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = getHouseOwner(
          request.getName(), request.getEmail(),
          request.getPassword(), request.getProfilePicture(),
          request.getCPR(), request.getPhone(),
          false, 0,
          request.getAddress(), request.getBiography());
      houseOwner.setUserId(houseOwnerRepository.save(houseOwner));

      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to update an existing house owner.
   * <p>
   * This method updates the details of an existing house owner and sends the updated house owner back in
   * the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the house owner to be updated.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void updateHouseOwner(UpdateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      HouseOwner houseOwner = getHouseOwner(request.getName(), "email",
          "password", request.getProfilePicture(),
          request.getCPR(), request.getPhone(),
          request.getIsVerified(), request.getAdminId(),
          request.getAddress(), request.getBiography());
      houseOwner.setUserId(request.getId());

      houseOwnerRepository.update(houseOwner);
      HouseOwnerResponse response = getHouseOwnerResponse(houseOwner);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to delete a house owner by their ID.
   * <p>
   * This method deletes the specified house owner and responds with an empty {@link HouseOwnerResponse}.
   * If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the ID of the house owner to be deleted.
   * @param responseObserver the observer used to send the response back to the client.
   */
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

  /**
   * Helper method to create a new {@link HouseOwner} object from the provided input values.
   *
   * @param name the name of the house owner.
   * @param email the email of the house owner.
   * @param password the password of the house owner.
   * @param profilePicture the profile picture URL of the house owner.
   * @param cpr the CPR (personal identification number) of the house owner.
   * @param phone the phone number of the house owner.
   * @param isVerified whether the house owner is verified.
   * @param adminId the ID of the administrator (if applicable).
   * @param address the address of the house owner.
   * @param biography the biography of the house owner.
   * @return a new {@link HouseOwner} object populated with the provided values.
   */
  private static HouseOwner getHouseOwner(String name, String email,
      String password, String profilePicture, String cpr, String phone,
      boolean isVerified, int adminId, String address, String biography)
  {
    HouseOwner houseOwner = new HouseOwner();
    houseOwner.setName(name);
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

  /**
   * Helper method to construct a {@link HouseOwnerResponse} object from a given {@link HouseOwner}.
   *
   * @param houseOwner the house owner entity to be converted into a response.
   * @return the corresponding {@link HouseOwnerResponse}.
   */
  private static HouseOwnerResponse getHouseOwnerResponse(HouseOwner houseOwner)
  {
    return HouseOwnerResponse.newBuilder()
        .setId(houseOwner.getUserId())
        .setName(houseOwner.getName())
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