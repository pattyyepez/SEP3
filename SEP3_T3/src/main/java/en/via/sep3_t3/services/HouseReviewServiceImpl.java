package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositories.HouseReviewRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class HouseReviewServiceImpl extends HouseReviewServiceGrpc.HouseReviewServiceImplBase {

  private final HouseReviewRepository houseReviewRepository;

  public HouseReviewServiceImpl(HouseReviewRepository houseReviewRepository) {
    this.houseReviewRepository = houseReviewRepository;
  }

  @Override
  public void getHouseReview(HouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      HouseReview houseReview = houseReviewRepository.findById(request.getId());
      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createHouseReview(CreateHouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      HouseReview houseReview = new HouseReview();
      houseReview.setProfile_id(request.getProfileId());
      houseReview.setSitter_id(request.getSitterId());
      houseReview.setRating(request.getRating());
      houseReview.setComment(request.getComment());
      houseReview.setDate(java.sql.Date.valueOf(request.getDate()));

      int id = houseReviewRepository.save(houseReview);
      houseReview.setId(id);

      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseReview(UpdateHouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      HouseReview houseReview = new HouseReview();
      houseReview.setId(request.getId());
      houseReview.setProfile_id(request.getProfileId());
      houseReview.setSitter_id(request.getSitterId());
      houseReview.setRating(request.getRating());
      houseReview.setComment(request.getComment());
      houseReview.setDate(java.sql.Date.valueOf(request.getDate()));

      houseReviewRepository.update(houseReview);
      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteHouseReview(DeleteHouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      houseReviewRepository.deleteById(request.getId());
      responseObserver.onNext(HouseReviewResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  private HouseReviewResponse buildHouseReviewResponse(HouseReview houseReview) {
    return HouseReviewResponse.newBuilder()
        .setId(houseReview.getId())
        .setProfileId(houseReview.getProfile_id())
        .setSitterId(houseReview.getSitter_id())
        .setRating(houseReview.getRating())
        .setComment(houseReview.getComment())
        .setDate(houseReview.getDate().toString())
        .build();
  }
}
