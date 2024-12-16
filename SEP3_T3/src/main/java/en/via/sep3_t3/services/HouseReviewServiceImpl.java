package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseReview;
import en.via.sep3_t3.repositoryContracts.IHouseReviewRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
      HouseReview houseReview = houseReviewRepository.findById(request.getProfileId(), request.getSitterId());
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
      houseReview.setDate(LocalDateTime.now());
      houseReviewRepository.save(houseReview);

      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(e);
    }
  }

  @Override public void updateHouseReview(UpdateHouseReviewRequest request,
      StreamObserver<HouseReviewResponse> responseObserver)
  {
    try
    {
      HouseReview houseReview = new HouseReview();
      houseReview.setProfile_id(request.getProfileId());
      houseReview.setSitter_id(request.getSitterId());
      houseReview.setRating(request.getRating());
      houseReview.setComment(request.getComment());
      houseReview = houseReviewRepository.update(houseReview);
      responseObserver.onNext(buildHouseReviewResponse(houseReview));
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
      houseReviewRepository.deleteById(request.getProfileId(), request.getSitterId());
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
    return HouseReviewResponse.newBuilder()
        .setProfileId(houseReview.getProfile_id())
        .setSitterId(houseReview.getSitter_id())
        .setRating(houseReview.getRating())
        .setComment(houseReview.getComment())
        .setDate(
            houseReview.getDate() != null ?
                ZonedDateTime.of(houseReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli() : 0)
        .build();
  }
}
