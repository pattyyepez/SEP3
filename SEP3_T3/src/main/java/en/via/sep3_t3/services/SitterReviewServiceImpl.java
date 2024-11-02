package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.SitterReview;
import en.via.sep3_t3.repositories.SitterReviewRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;


@Service
public class SitterReviewServiceImpl extends SitterReviewServiceGrpc.SitterReviewServiceImplBase {

  private final SitterReviewRepository sitterReviewRepository;

  public SitterReviewServiceImpl(SitterReviewRepository sitterReviewRepository) {
    this.sitterReviewRepository = sitterReviewRepository;
  }

  @Override
  public void getSitterReview(SitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview sitterReview = sitterReviewRepository.findById(request.getId());
      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createSitterReview(CreateSitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview sitterReview = new SitterReview();
      sitterReview.setOwner_id(request.getOwnerId());
      sitterReview.setSitter_id(request.getSitterId());
      sitterReview.setRating(request.getRating());
      sitterReview.setComment(request.getComment());
      sitterReview.setDate(java.sql.Date.valueOf(request.getDate()));

      sitterReviewRepository.save(sitterReview);

      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateSitterReview(UpdateSitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview sitterReview = new SitterReview();
      sitterReview.setId(request.getId());
      sitterReview.setOwner_id(request.getOwnerId());
      sitterReview.setSitter_id(request.getSitterId());
      sitterReview.setRating(request.getRating());
      sitterReview.setComment(request.getComment());
      sitterReview.setDate(java.sql.Date.valueOf(request.getDate()));

      sitterReviewRepository.update(sitterReview);

      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteSitterReview(DeleteSitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      sitterReviewRepository.deleteById(request.getId());
      responseObserver.onNext(SitterReviewResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }


  private SitterReviewResponse buildSitterReviewResponse(SitterReview sitterReview) {
    return SitterReviewResponse.newBuilder()
        .setId(sitterReview.getId())
        .setOwnerId(sitterReview.getOwner_id())
        .setSitterId(sitterReview.getSitter_id())
        .setRating(sitterReview.getRating())
        .setComment(sitterReview.getComment())
        .setDate(sitterReview.getDate().toString())
        .build();
  }
}
