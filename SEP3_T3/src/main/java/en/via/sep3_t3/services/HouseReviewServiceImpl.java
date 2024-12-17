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

/**
 * Implementation of the House Review Service that provides gRPC endpoints to manage house reviews.
 * <p>
 * This service allows clients to retrieve, create, update, and delete house reviews, as well as
 * fetch all reviews in the system. It uses an {@link IHouseReviewRepository} for database
 * interactions.
 * </p>
 */
@Service
public class HouseReviewServiceImpl extends HouseReviewServiceGrpc.HouseReviewServiceImplBase {

  private final IHouseReviewRepository houseReviewRepository;

  /**
   * Constructs a new HouseReviewServiceImpl with the specified repository.
   *
   * @param houseReviewRepository the repository used for house review data operations.
   */
  public HouseReviewServiceImpl(IHouseReviewRepository houseReviewRepository) {
    this.houseReviewRepository = houseReviewRepository;
  }

  /**
   * Retrieves a specific house review based on the composite IDs: profile ID and sitter ID.
   *
   * @param request the gRPC request containing the profile ID and sitter ID of the review.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getHouseReview(HouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      HouseReview houseReview = houseReviewRepository.findById(request.getProfileId(), request.getSitterId());
      HouseReviewResponse response = buildHouseReviewResponse(houseReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Retrieves all house reviews in the system.
   *
   * @param request the gRPC request to fetch all house reviews.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getAllHouseReviews(AllHouseReviewsRequest request, StreamObserver<AllHouseReviewsResponse> responseObserver) {
    try {
      List<HouseReview> houseReviews = houseReviewRepository.findAll();
      List<HouseReviewResponse> houseReviewResponses = new ArrayList<>();

      for (HouseReview houseReview : houseReviews) {
        houseReviewResponses.add(buildHouseReviewResponse(houseReview));
      }

      AllHouseReviewsResponse response = AllHouseReviewsResponse.newBuilder()
          .addAllHouseReviews(houseReviewResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Creates a new house review.
   *
   * @param request the gRPC request containing the details of the new house review.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void createHouseReview(CreateHouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
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
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Updates an existing house review.
   *
   * @param request the gRPC request containing the updated details of the house review.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void updateHouseReview(UpdateHouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      HouseReview houseReview = new HouseReview();
      houseReview.setProfile_id(request.getProfileId());
      houseReview.setSitter_id(request.getSitterId());
      houseReview.setRating(request.getRating());
      houseReview.setComment(request.getComment());
      houseReview.setDate(LocalDateTime.now());
      houseReview = houseReviewRepository.update(houseReview);
      responseObserver.onNext(buildHouseReviewResponse(houseReview));
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Deletes a specific house review based on the composite IDs: profile ID and sitter ID.
   *
   * @param request the gRPC request containing the profile ID and sitter ID of the review to delete.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void deleteHouseReview(HouseReviewRequest request, StreamObserver<HouseReviewResponse> responseObserver) {
    try {
      houseReviewRepository.deleteById(request.getProfileId(), request.getSitterId());
      responseObserver.onNext(HouseReviewResponse.newBuilder().build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Helper method to build a {@link HouseReviewResponse} from a {@link HouseReview} entity.
   *
   * @param houseReview the {@link HouseReview} entity containing review details.
   * @return a {@link HouseReviewResponse} containing the review information.
   */
  private HouseReviewResponse buildHouseReviewResponse(HouseReview houseReview) {
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

