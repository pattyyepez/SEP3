package en.via.sep3_t3.server;

import en.via.sep3_t3.services.*;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Configuration class for setting up the gRPC server.
 * This class initializes a Netty-based gRPC server and registers various service implementations.
 */
@Configuration
public class GrpcServerConfig {

  /**
   * The gRPC server instance.
   */
  private Server server;

  /**
   * Configures and starts the gRPC server with the provided service implementations.
   *
   * <p>The server listens on port 9090 and registers the following services:</p>
   * <ul>
   *   <li>{@link HouseOwnerServiceImpl}</li>
   *   <li>{@link HouseSitterServiceImpl}</li>
   *   <li>{@link HouseProfileServiceImpl}</li>
   *   <li>{@link HouseListingServiceImpl}</li>
   *   <li>{@link ApplicationServiceImpl}</li>
   *   <li>{@link HouseReviewServiceImpl}</li>
   *   <li>{@link SitterReviewServiceImpl}</li>
   *   <li>{@link ReportServiceImpl}</li>
   * </ul>
   *
   * @param houseOwnerService the service implementation for house owners.
   * @param houseSitterService the service implementation for house sitters.
   * @param houseProfileService the service implementation for house profiles.
   * @param houseListingService the service implementation for house listings.
   * @param applicationService the service implementation for applications.
   * @param houseReviewService the service implementation for house reviews.
   * @param sitterReviewService the service implementation for sitter reviews.
   * @param reportService the service implementation for reports.
   * @return the configured and started {@link Server} instance.
   * @throws IOException if the server fails to start.
   */
  @Bean
  public Server grpcServer(
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

  /**
   * Stops the gRPC server when the application context is being shutdown.
   *
   * <p>This method is annotated with {@link PreDestroy} to ensure it is called during
   * the application's shutdown sequence.</p>
   */
  @PreDestroy
  public void stopGrpcServer() {
    if (server != null) {
      server.shutdown();
      System.out.println("gRPC Server stopped.");
    }
  }
}

