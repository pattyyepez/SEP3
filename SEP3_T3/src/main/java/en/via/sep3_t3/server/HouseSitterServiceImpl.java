package en.via.sep3_t3.server;

import en.via.sep3_t3.*;
import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositories.HouseSitterRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class HouseSitterServiceImpl extends HouseSitterServiceGrpc.HouseSitterServiceImplBase {

  private final HouseSitterRepository houseSitterRepository;

  public HouseSitterServiceImpl(HouseSitterRepository houseSitterRepository) {
    this.houseSitterRepository = houseSitterRepository;
  }

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

  @Override
  public void createHouseSitter(CreateHouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      HouseSitter houseSitter = getHouseSitter(request.getEmail(), request.getPassword(), request.getProfilePicture(),
          request.getCPR(), request.getPhone(), request.getIsVerified(), request.getAdminId(),
          request.getExperience(), request.getBiography());
      houseSitter.setUserId(houseSitterRepository.save(houseSitter));

      HouseSitterResponse response = getHouseSitterResponse(houseSitter);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateHouseSitter(UpdateHouseSitterRequest request, StreamObserver<HouseSitterResponse> responseObserver) {
    try {
      HouseSitter houseSitter = getHouseSitter(request.getEmail(), request.getPassword(), request.getProfilePicture(),
          request.getCPR(), request.getPhone(), request.getIsVerified(), request.getAdminId(),
          request.getExperience(), request.getBiography());
      houseSitter.setUserId(request.getId());

      houseSitterRepository.update(houseSitter);
      HouseSitterResponse response = getHouseSitterResponse(houseSitter);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

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

  private static HouseSitter getHouseSitter(String email, String password, String profilePicture, String cpr,
      String phone, boolean isVerified, int adminId, String experience, String biography) {
    HouseSitter houseSitter = new HouseSitter();
    houseSitter.setEmail(email);
    houseSitter.setPassword(password);
    houseSitter.setProfilePicture(profilePicture);
    houseSitter.setCPR(cpr);
    houseSitter.setPhone(phone);
    houseSitter.setVerified(isVerified);
    houseSitter.setAdminId(adminId);
    houseSitter.setExperience(experience);
    houseSitter.setBiography(biography);
    return houseSitter;
  }

  private static HouseSitterResponse getHouseSitterResponse(HouseSitter houseSitter) {
    return HouseSitterResponse.newBuilder()
        .setId(houseSitter.getUserId())
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
