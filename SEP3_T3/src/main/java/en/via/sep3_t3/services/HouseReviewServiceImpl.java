package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service public class HouseReviewServiceImpl
    extends HouseReviewServiceGrpc.HouseReviewServiceImplBase
{

  private final IHouseReviewRepository houseReviewRepository;

  public HouseReviewServiceImpl(IHouseReviewRepository houseReviewRepository)
  {
    this.houseReviewRepository = houseReviewRepository;
  }

  @Override public void getHouseReview(HouseReviewRequest request,
      StreamObserver<HouseReviewResponse> responseObserver)
  {
    try
    {
      HouseReview houseReview = houseReviewRepository.findById(request.getId());
      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getAllHouseReviews(AllHouseReviewsRequest request,
      StreamObserver<AllHouseReviewsResponse> responseObserver)
  {
    try
    {
      List<HouseReview> houseReviews = houseReviewRepository.findAll();
      List<HouseReviewResponse> houseReviewResponses = new ArrayList<>();

      for (HouseReview houseReview : houseReviews)
      {
        houseReviewResponses.add(buildHouseReviewResponse(houseReview));
      }

      AllHouseReviewsResponse response = AllHouseReviewsResponse.newBuilder()
          .addAllHouseReviews(houseReviewResponses).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override public void createHouseReview(CreateHouseReviewRequest request,
      StreamObserver<HouseReviewResponse> responseObserver)
  {
    try
    {
      HouseReview houseReview = new HouseReview();
      houseReview.setProfile_id(request.getProfileId());
      houseReview.setSitter_id(request.getSitterId());
      houseReview.setRating(request.getRating());
      houseReview.setComment(request.getComment());
      houseReview.setDate(Timestamp.valueOf(LocalDateTime.now()));

      int id = houseReviewRepository.save(houseReview);
      houseReview.setId(id);

      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override public void deleteHouseReview(HouseReviewRequest request,
      StreamObserver<HouseReviewResponse> responseObserver)
  {
    try
    {
      houseReviewRepository.deleteById(request.getId());
      responseObserver.onNext(HouseReviewResponse.newBuilder().build());
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  private HouseReviewResponse buildHouseReviewResponse(HouseReview houseReview)
  {
    return HouseReviewResponse.newBuilder().setId(houseReview.getId())
        .setProfileId(houseReview.getProfile_id())
        .setSitterId(houseReview.getSitter_id())
        .setRating(houseReview.getRating()).setComment(houseReview.getComment())
        .setDate(
            houseReview.getDate() != null ? houseReview.getDate().getTime() : 0)
        .build();
  }
}
