package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositoryContracts.IHouseListingRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the House Listing Service that handles gRPC requests related to house listings.
 * <p>
 * This service provides functionality for managing house listings, including retrieving, creating,
 * updating, and deleting house listings. It communicates with the {@link IHouseListingRepository} to persist
 * and retrieve house listing data from the underlying database.
 * </p>
 */
@Service
public class HouseListingServiceImpl extends HouseListingServiceGrpc.HouseListingServiceImplBase {

  private final IHouseListingRepository houseListingRepository;

  /**
   * Constructs the HouseListingServiceImpl with the provided repository.
   *
   * @param houseListingRepository the repository used to interact with the house listing data store.
   */
  public HouseListingServiceImpl(IHouseListingRepository houseListingRepository) {
    this.houseListingRepository = houseListingRepository;
  }

  /**
   * Handles the request to get a house listing by its ID.
   * <p>
   * This method retrieves a specific house listing based on the provided ID and responds
   * with the corresponding {@link HouseListingResponse}. If the listing is found, the response is
   * sent to the client. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the ID of the house listing.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getHouseListing(HouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      HouseListing houseListing = houseListingRepository.findById(request.getId());
      HouseListingResponse response = buildHouseListingResponse(houseListing);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to get all house listings.
   * <p>
   * This method retrieves all house listings from the database and constructs a response containing a list
   * of {@link HouseListingResponse} objects. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request to get all house listings.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void getAllHouseListings(AllHouseListingsRequest request, StreamObserver<AllHouseListingsResponse> responseObserver) {
    try {
      List<HouseListing> houseListings = houseListingRepository.findAll();
      List<HouseListingResponse> houseListingResponses = new ArrayList<>();

      for (HouseListing houseListing : houseListings) {
        houseListingResponses.add(buildHouseListingResponse(houseListing));
      }

      AllHouseListingsResponse response = AllHouseListingsResponse.newBuilder()
          .addAllHouseListings(houseListingResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to create a new house listing.
   * <p>
   * This method creates a new house listing with the provided data, saves it to the database, and sends the
   * created house listing back in the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the house listing to be created.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void createHouseListing(CreateHouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      HouseListing houseListing = new HouseListing();
      houseListing.setProfile_id(request.getProfileId());
      houseListing.setStartDate(new Date(request.getStartDate()));
      houseListing.setEndDate(new Date(request.getEndDate()));
      houseListing.setStatus("Open");

      int id = houseListingRepository.save(houseListing);
      houseListing.setId(id);

      HouseListingResponse response = buildHouseListingResponse(houseListing);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to update an existing house listing.
   * <p>
   * This method updates the status of an existing house listing and sends the updated house listing back in
   * the response. If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the details of the house listing to be updated.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void updateHouseListing(UpdateHouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      HouseListing houseListing = new HouseListing();
      houseListing.setId(request.getId());
      houseListing.setStatus(request.getStatus());

      houseListing = houseListingRepository.update(houseListing);
      HouseListingResponse response = buildHouseListingResponse(houseListing);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Handles the request to delete a house listing by its ID.
   * <p>
   * This method deletes the specified house listing and responds with an empty {@link HouseListingResponse}.
   * If an error occurs, it sends an error to the client.
   * </p>
   *
   * @param request the gRPC request containing the ID of the house listing to be deleted.
   * @param responseObserver the observer used to send the response back to the client.
   */
  @Override
  public void deleteHouseListing(HouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      houseListingRepository.deleteById(request.getId());
      HouseListingResponse response = HouseListingResponse.newBuilder().build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Constructs a {@link HouseListingResponse} object from the given {@link HouseListing} entity.
   *
   * @param houseListing the house listing entity to be converted to a response.
   * @return the constructed {@link HouseListingResponse}.
   */
  private HouseListingResponse buildHouseListingResponse(HouseListing houseListing) {
    return HouseListingResponse.newBuilder()
        .setId(houseListing.getId())
        .setProfileId(houseListing.getProfile_id())
        .setStartDate(houseListing.getStartDate() != null ? houseListing.getStartDate().getTime() : 0)
        .setEndDate(houseListing.getEndDate() != null ? houseListing.getEndDate().getTime() : 0)
        .setStatus(houseListing.getStatus())
        .build();
  }
}
