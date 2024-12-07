package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.SitterReview;
import en.via.sep3_t3.repositoryContracts.ISitterReviewRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SitterReviewServiceImpl extends SitterReviewServiceGrpc.SitterReviewServiceImplBase {

  private final ISitterReviewRepository sitterReviewRepository;

  public SitterReviewServiceImpl(ISitterReviewRepository sitterReviewRepository) {
    this.sitterReviewRepository = sitterReviewRepository;
  }

  @Override
  public void getSitterReview(SitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview sitterReview = sitterReviewRepository.findById(request.getOwnerId(), request.getSitterId());
      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getAllSitterReviews(AllSitterReviewsRequest request,
      StreamObserver<AllSitterReviewsResponse> responseObserver)
  {
    try
    {
      List<SitterReview> sitterReviews = sitterReviewRepository.findAll();
      List<SitterReviewResponse> sitterReviewResponses = new ArrayList<>();

      for (SitterReview sitterReview : sitterReviews)
      {
        sitterReviewResponses.add(buildSitterReviewResponse(sitterReview));
      }

      AllSitterReviewsResponse response = AllSitterReviewsResponse.newBuilder()
          .addAllSitterReviews(sitterReviewResponses).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
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
      sitterReview.setDate(LocalDateTime.now());

      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override public void updateSitterReview(UpdateSitterReviewRequest request,
      StreamObserver<SitterReviewResponse> responseObserver)
  {
    try
    {
      SitterReview review = new SitterReview();
      review.setOwner_id(request.getOwnerId());
      review.setSitter_id(request.getSitterId());
      review.setRating(request.getRating());
      review.setComment(request.getComment());
      review = sitterReviewRepository.update(review);
      responseObserver.onNext(buildSitterReviewResponse(review));
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteSitterReview(SitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      sitterReviewRepository.deleteById(request.getOwnerId(), request.getSitterId());
      responseObserver.onNext(SitterReviewResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }


  private SitterReviewResponse buildSitterReviewResponse(SitterReview sitterReview) {
    return SitterReviewResponse.newBuilder()
        .setOwnerId(sitterReview.getOwner_id())
        .setSitterId(sitterReview.getSitter_id())
        .setRating(sitterReview.getRating())
        .setComment(sitterReview.getComment())
        .setDate(sitterReview.getDate() != null ? ZonedDateTime.of(sitterReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli() : 0)
        .build();
  }
}
