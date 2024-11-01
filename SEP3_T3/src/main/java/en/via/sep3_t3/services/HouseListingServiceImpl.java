package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseListing;
import en.via.sep3_t3.repositories.HouseListingRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HouseListingServiceImpl extends HouseListingServiceGrpc.HouseListingServiceImplBase {

  private final HouseListingRepository houseListingRepository;

  public HouseListingServiceImpl(HouseListingRepository houseListingRepository) {
    this.houseListingRepository = houseListingRepository;
  }

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

  @Override
  public void createHouseListing(CreateHouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      HouseListing houseListing = new HouseListing();
      houseListing.setProfile_id(request.getProfileId());
      houseListing.setStartDate(new Date(request.getStartDate()));
      houseListing.setEndDate(new Date(request.getEndDate()));
      houseListing.setStatus(request.getStatus());

      int id = houseListingRepository.save(houseListing);
      houseListing.setId(id);

      HouseListingResponse response = buildHouseListingResponse(houseListing);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseListing(UpdateHouseListingRequest request, StreamObserver<HouseListingResponse> responseObserver) {
    try {
      HouseListing houseListing = new HouseListing();
      houseListing.setId(request.getId());
      houseListing.setProfile_id(request.getProfileId());
      houseListing.setStartDate(new Date(request.getStartDate()));
      houseListing.setEndDate(new Date(request.getEndDate()));
      houseListing.setStatus(request.getStatus());

      houseListingRepository.update(houseListing);
      HouseListingResponse response = buildHouseListingResponse(houseListing);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

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

  private HouseListingResponse buildHouseListingResponse(HouseListing houseListing) {
    return HouseListingResponse.newBuilder()
        .setId(houseListing.getId())
        .setProfileId(houseListing.getProfile_id())
        .setStartDate(houseListing.getStartDate().getTime())
        .setEndDate(houseListing.getEndDate().getTime())
        .setStatus(houseListing.getStatus())
        .build();
  }
}
