//package en.via.sep3_t3.server;
//
//import en.via.sep3_t3.repositories.HouseOwnerRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class HousePalServiceImpl extends HouseOwnerServiceGrpc.HouseOwnerServiceImplBase {
//
//  private final HouseOwnerRepository houseOwnerRepository;
//
//  public HousePalServiceImpl(HouseOwnerRepository houseOwnerRepository) {
//    this.houseOwnerRepository = houseOwnerRepository;
//  }
//
//  @Override
//  public void getHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
//    try {
//      dk.via.sep.domain.HouseOwner houseOwner = houseOwnerRepository.findById(request.getOwnerId());
//      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
//          .setOwnerId(houseOwner.getOwnerId())
//          .setAddress(houseOwner.getAddress())
//          .setBiography(houseOwner.getBiography())
//          .build();
//      responseObserver.onNext(response);
//      responseObserver.onCompleted();
//    } catch (Exception e) {
//      responseObserver.onError(e);
//    }
//  }
//
//  @Override
//  public void createHouseOwner(CreateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
//    try {
//      HouseOwner houseOwner = new dk.via.sep.domain.HouseOwner();
//      //      houseOwner.setOwnerId(request.getOwnerId());
//      houseOwner.setAddress(request.getAddress());
//      houseOwner.setBiography(request.getBiography());
//
//      int id = houseOwnerRepository.save(houseOwner);
//
//      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
//          .setOwnerId(id)
//          .setAddress(request.getAddress())
//          .setBiography(request.getBiography())
//          .build();
//
//      responseObserver.onNext(response);
//      responseObserver.onCompleted();
//    } catch (Exception e) {
//      responseObserver.onError(e);
//    }
//  }
//
//  @Override
//  public void updateHouseOwner(UpdateHouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
//    try {
//      dk.via.sep.domain.HouseOwner houseOwner = new dk.via.sep.domain.HouseOwner();
//      houseOwner.setOwnerId(request.getOwnerId());
//      houseOwner.setAddress(request.getAddress());
//      houseOwner.setBiography(request.getBiography());
//
//      houseOwnerRepository.update(houseOwner);
//
//      HouseOwnerResponse response = HouseOwnerResponse.newBuilder()
//          .setOwnerId(request.getOwnerId())
//          .setAddress(request.getAddress())
//          .setBiography(request.getBiography())
//          .build();
//
//      responseObserver.onNext(response);
//      responseObserver.onCompleted();
//    } catch (Exception e) {
//      responseObserver.onError(e);
//    }
//  }
//
//  @Override
//  public void deleteHouseOwner(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver) {
//    try {
//      houseOwnerRepository.deleteById(request.getOwnerId());
//
//      HouseOwnerResponse response = HouseOwnerResponse.newBuilder().build();
//
//      responseObserver.onNext(response);
//      responseObserver.onCompleted();
//    } catch (Exception e) {
//      responseObserver.onError(e);
//    }
//  }
//}
