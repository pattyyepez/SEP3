package dk.via.sep.server;

import dk.via.sep.HouseOwner;
import dk.via.sep.HouseOwnerRequest;
import dk.via.sep.HouseOwnerResponse;
import dk.via.sep.HouseOwnerServiceGrpc;
import dk.via.sep.repositories.HouseOwnerRepository;
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
      dk.via.sep.domain.HouseOwner houseOwner = houseOwnerRepository.findById(request.getOwnerId());
      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
          .setHouseOwner(HouseOwner.newBuilder()
              .setOwnerId(houseOwner.getOwnerId())
              .setAddress(houseOwner.getAddress())
              .setBiography(houseOwner.getBiography())
              .build())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createHouseOwner(HouseOwner request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      dk.via.sep.domain.HouseOwner houseOwner = new dk.via.sep.domain.HouseOwner();
      houseOwner.setOwnerId(request.getOwnerId());
      houseOwner.setAddress(request.getAddress());
      houseOwner.setBiography(request.getBiography());

      houseOwnerRepository.save(houseOwner);

      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
          .setHouseOwner(request)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseOwner(HouseOwner request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      dk.via.sep.domain.HouseOwner houseOwner = new dk.via.sep.domain.HouseOwner();
      houseOwner.setOwnerId(request.getOwnerId());
      houseOwner.setAddress(request.getAddress());
      houseOwner.setBiography(request.getBiography());

      houseOwnerRepository.update(houseOwner);

      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
          .setHouseOwner(request)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
    try {
      houseOwnerRepository.deleteById(request.getOwnerId());

      HouseOwnerResponse response = HouseOwnerResponse.newBuilder().build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
