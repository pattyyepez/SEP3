package dk.via.sep.server;

import dk.via.sep.repositories.HouseOwnerRepository;
import dk.via.sep.server.HousePalServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

public class HousePalServer {

  private HouseOwnerRepository houseOwnerRepository;

  public static void main(String[] args) throws Exception {
    new HousePalServer().run();
  }

  private void run() throws Exception {
    DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/housePalBD", "postgres", "calculadora11");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    houseOwnerRepository = new HouseOwnerRepository(jdbcTemplate);
    HousePalServiceImpl housePalService = new HousePalServiceImpl(houseOwnerRepository);

    Server server = ServerBuilder
        .forPort(9090)
        .addService(housePalService)
        .build();

    server.start();
    System.out.println("HousePal.....");
    server.awaitTermination();
  }
}
