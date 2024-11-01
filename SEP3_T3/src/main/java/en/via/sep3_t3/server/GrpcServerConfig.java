package en.via.sep3_t3.server;

import en.via.sep3_t3.services.*;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class GrpcServerConfig {

  private Server server;

  public Server createGrpcServer(
      HouseOwnerServiceImpl houseOwnerService,
      HouseSitterServiceImpl houseSitterService,
      HouseProfileServiceImpl houseProfileService,
      HouseListingServiceImpl houseListingService,
      ApplicationServiceImpl applicationService,
      HouseReviewServiceImpl houseReviewService,
      SitterReviewServiceImpl sitterReviewService,
      ReportServiceImpl reportService
  ) throws IOException {
    server = NettyServerBuilder.forPort(9090)
        .addService(houseOwnerService)
        .addService(houseSitterService)
        .addService(houseProfileService)
        .addService(houseListingService)
        .addService(applicationService)
        .addService(houseReviewService)
        .addService(sitterReviewService)
        .addService(reportService)
        .build()
        .start();
    System.out.println("gRPC Server started on port 9090...");
    return server;
  }

  @PreDestroy
  public void stopGrpcServer() {
    if (server != null) {
      server.shutdown();
      System.out.println("gRPC Server stopped.");
    }
  }
}
