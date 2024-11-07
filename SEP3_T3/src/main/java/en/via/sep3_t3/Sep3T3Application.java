package en.via.sep3_t3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaRepositories(basePackages = {"en.via.sep3_t3.repositoryContracts"})

@SpringBootApplication public class Sep3T3Application
{

  public static void main(String[] args)
  {
    SpringApplication.run(Sep3T3Application.class, args);
  }

}
