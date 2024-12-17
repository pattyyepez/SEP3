package en.via.sep3_t3.services;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositoryContracts.IHouseSitterRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the House Sitter Service providing gRPC endpoints for house sitter management.
 * <p>
 * This service allows clients to perform CRUD operations on house sitters, fetch all house sitters,
 * retrieve specific skills, and manage sitter details. The service interacts with an
 * {@link IHouseSitterRepository} for persistence.
 * </p>
 */
@Service
public class HouseSitterServiceImpl extends HouseSitterServiceGrpc.HouseSitterServiceImplBase {

  private final IHouseSitterRepository houseSitterRepository;

  /**
   * Constructs a new HouseSitterServiceImpl with the given repository.
   *
   * @param houseSitterRepository the repository used for house sitter data operations.
   */
  public HouseSitterServiceImpl(IHouseSitterRepository houseSitterRepository) {
    this.houseSitterRepository = houseSitterRepository;
  }

  /**
   * Retrieves a house sitter by their ID.
   *
   * @param request the gRPC request containing the ID of the house sitter to retrieve.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getHouseSitter(HouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      HouseSitter houseSitter = houseSitterRepository.findById(request.getId());
      HouseSitterResponse response = getHouseSitterResponse(houseSitter);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Retrieves all house sitters in the system.
   *
   * @param request the gRPC request to fetch all house sitters.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getAllHouseSitters(AllHouseSittersRequest request, StreamObserver<AllHouseSittersResponse> responseObserver) {
    try {
      List<HouseSitter> houseSitters = houseSitterRepository.findAll();
      List<HouseSitterResponse> houseSitterResponses = new ArrayList<>();

      for (HouseSitter houseSitter : houseSitters) {
        houseSitterResponses.add(getHouseSitterResponse(houseSitter));
      }

      AllHouseSittersResponse response = AllHouseSittersResponse.newBuilder()
          .addAllHouseSitters(houseSitterResponses)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Creates a new house sitter.
   *
   * @param request the gRPC request containing details of the house sitter to create.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void createHouseSitter(CreateHouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      HouseSitter houseSitter = getHouseSitter(
          request.getName(), request.getEmail(), request.getPassword(), request.getProfilePicture(),
          request.getCPR(), request.getPhone(), false, 0,
          request.getExperience(), request.getBiography(),
          request.getSkillsList().stream().toList(), request.getPicturesList().stream().toList());
      houseSitter.setUserId(houseSitterRepository.save(houseSitter));

      HouseSitterResponse response = getHouseSitterResponse(houseSitter);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Updates an existing house sitter.
   *
   * @param request the gRPC request containing updated details of the house sitter.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void updateHouseSitter(UpdateHouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      HouseSitter houseSitter = getHouseSitter(
          request.getName(), "email", "password", request.getProfilePicture(),
          request.getCPR(), request.getPhone(), request.getIsVerified(), request.getAdminId(),
          request.getExperience(), request.getBiography(),
          request.getSkillsList().stream().toList(), request.getPicturesList().stream().toList());
      houseSitter.setUserId(request.getId());

      houseSitterRepository.update(houseSitter);
      HouseSitterResponse response = getHouseSitterResponse(houseSitter);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Deletes a house sitter by their ID.
   *
   * @param request the gRPC request containing the ID of the house sitter to delete.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void deleteHouseSitter(HouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      houseSitterRepository.deleteById(request.getId());
      HouseSitterResponse response = HouseSitterResponse.newBuilder().build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Retrieves all available skills for house sitters.
   *
   * @param request the gRPC request to fetch all skills.
   * @param responseObserver the observer used to send the response or error back to the client.
   */
  @Override
  public void getAllSkills(AllSkillsRequest request, StreamObserver<AllSkillsResponse> responseObserver) {
    try {
      List<String> skills = houseSitterRepository.findAllSkills();
      AllSkillsResponse response = AllSkillsResponse.newBuilder().addAllSkill(skills).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Creates a new {@link HouseSitter} entity with the given parameters.
   *
   * @param name the name of the house sitter.
   * @param email the email address of the house sitter.
   * @param password the password for the house sitter's account.
   * @param profilePicture the profile picture URL of the house sitter.
   * @param cpr the CPR (personal ID number) of the house sitter.
   * @param phone the phone number of the house sitter.
   * @param isVerified whether the house sitter is verified.
   * @param adminId the admin ID associated with this sitter.
   * @param experience the house sitter's experience description.
   * @param biography the house sitter's biography.
   * @param skills a list of skills the house sitter possesses.
   * @param pictures a list of picture URLs related to the house sitter's profile.
   * @return a populated {@link HouseSitter} object.
   */
  private static HouseSitter getHouseSitter(String name, String email, String password, String profilePicture, String cpr,
      String phone, boolean isVerified, int adminId, String experience, String biography, List<String> skills, List<String> pictures) {
    HouseSitter houseSitter = new HouseSitter();
    houseSitter.setName(name);
    houseSitter.setEmail(email);
    houseSitter.setPassword(password);
    houseSitter.setProfilePicture(profilePicture);
    houseSitter.setCPR(cpr);
    houseSitter.setPhone(phone);
    houseSitter.setVerified(isVerified);
    houseSitter.setAdminId(adminId);
    houseSitter.setExperience(experience);
    houseSitter.setBiography(biography);
    houseSitter.setSkills(skills);
    houseSitter.setPictures(pictures);
    return houseSitter;
  }

  /**
   * Converts a {@link HouseSitter} entity to a {@link HouseSitterResponse}.
   *
   * @param houseSitter the {@link HouseSitter} entity to convert.
   * @return a {@link HouseSitterResponse} object.
   */
  private static HouseSitterResponse getHouseSitterResponse(HouseSitter houseSitter) {
    return HouseSitterResponse.newBuilder()
        .setId(houseSitter.getUserId())
        .setName(houseSitter.getName())
        .setEmail(houseSitter.getEmail())
        .setPassword(houseSitter.getPassword())
        .setProfilePicture(houseSitter.getProfilePicture())
        .setCPR(houseSitter.getCPR())
        .setPhone(houseSitter.getPhone())
        .setIsVerified(houseSitter.isVerified())
        .setAdminId(houseSitter.getAdminId())
        .setExperience(houseSitter.getExperience())
        .setBiography(houseSitter.getBiography())
        .addAllPictures(houseSitter.getPictures())
        .addAllSkills(houseSitter.getSkills())
        .build();
  }
}

