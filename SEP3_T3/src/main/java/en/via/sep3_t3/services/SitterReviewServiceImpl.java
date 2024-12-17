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

/**
 * Implementation of the Sitter Review Service providing gRPC endpoints for managing sitter reviews.
 * <p>
 * This service allows clients to create, retrieve, update, and delete reviews for sitters.
 * It communicates with the {@link ISitterReviewRepository} to handle database operations.
 * </p>
 */
@Service
public class SitterReviewServiceImpl extends SitterReviewServiceGrpc.SitterReviewServiceImplBase {

  private final ISitterReviewRepository sitterReviewRepository;

  /**
   * Constructs a new SitterReviewServiceImpl with the given repository.
   *
   * @param sitterReviewRepository the repository used for sitter review operations.
   */
  public SitterReviewServiceImpl(ISitterReviewRepository sitterReviewRepository) {
    this.sitterReviewRepository = sitterReviewRepository;
  }

  /**
   * Retrieves a specific sitter review based on the owner ID and sitter ID.
   *
   * @param request the gRPC request containing owner and sitter IDs.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Retrieves all sitter reviews.
   *
   * @param request the gRPC request to fetch all sitter reviews.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getAllSitterReviews(AllSitterReviewsRequest request, StreamObserver<AllSitterReviewsResponse> responseObserver) {
    try {
      List<SitterReview> sitterReviews = sitterReviewRepository.findAll();
      List<SitterReviewResponse> sitterReviewResponses = new ArrayList<>();

      for (SitterReview sitterReview : sitterReviews) {
        sitterReviewResponses.add(buildSitterReviewResponse(sitterReview));
      }

      AllSitterReviewsResponse response = AllSitterReviewsResponse.newBuilder()
          .addAllSitterReviews(sitterReviewResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Creates a new sitter review.
   *
   * @param request the gRPC request containing details of the review to create.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void createSitterReview(CreateSitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview sitterReview = new SitterReview();
      sitterReview.setOwner_id(request.getOwnerId());
      sitterReview.setSitter_id(request.getSitterId());
      sitterReview.setRating(request.getRating());
      sitterReview.setComment(request.getComment());
      sitterReview.setDate(LocalDateTime.now());

      sitterReviewRepository.save(sitterReview);

      SitterReviewResponse response = buildSitterReviewResponse(sitterReview);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Updates an existing sitter review.
   *
   * @param request the gRPC request containing updated review details.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void updateSitterReview(UpdateSitterReviewRequest request, StreamObserver<SitterReviewResponse> responseObserver) {
    try {
      SitterReview review = new SitterReview();
      review.setOwner_id(request.getOwnerId());
      review.setSitter_id(request.getSitterId());
      review.setRating(request.getRating());
      review.setComment(request.getComment());
      review.setDate(LocalDateTime.now());

      review = sitterReviewRepository.update(review);

      SitterReviewResponse response = buildSitterReviewResponse(review);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Deletes a sitter review based on the owner ID and sitter ID.
   *
   * @param request the gRPC request containing the IDs of the review to delete.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
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

  /**
   * Converts a {@link SitterReview} entity to a {@link SitterReviewResponse}.
   *
   * @param sitterReview the {@link SitterReview} entity to convert.
   * @return a {@link SitterReviewResponse} object containing the review data.
   */
  private SitterReviewResponse buildSitterReviewResponse(SitterReview sitterReview) {
    return SitterReviewResponse.newBuilder()
        .setOwnerId(sitterReview.getOwner_id())
        .setSitterId(sitterReview.getSitter_id())
        .setRating(sitterReview.getRating())
        .setComment(sitterReview.getComment())
        .setDate(sitterReview.getDate() != null
            ? ZonedDateTime.of(sitterReview.getDate(), ZoneId.systemDefault()).toInstant().toEpochMilli()
            : 0)
        .build();
  }
}
