package en.via.sep3_t3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * Main class for running the Spring Boot application.
 * <p>
 * This class is responsible for starting the Spring Boot application and enabling
 * JPA repositories located in the specified package.
 * </p>
 *
 * @EnableJpaRepositories: Enables Spring Data JPA repositories and configures them
 *                         to be scanned from the specified base package.
 * @SpringBootApplication: Marks the class as a Spring Boot application entry point.
 * The {@link #main(String[])} method runs the application and initializes the Spring context.
 */
@EnableJpaRepositories(basePackages = {"en.via.sep3_t3.repoDataValidationProxies"})
@SpringBootApplication
public class Sep3T3Application {

  /**
   * The main entry point of the Spring Boot application.
   * <p>
   * This method launches the application by calling {@link SpringApplication#run(Class, String...)}.
   * It also sets up the Spring context and starts all necessary components in the application.
   * </p>
   *
   * @param args Some arguments passed to the main class when running the app.
   * @see SpringApplication#run(Class, String...)
   */
  public static void main(String[] args) {
    SpringApplication.run(Sep3T3Application.class, args);
  }
}

