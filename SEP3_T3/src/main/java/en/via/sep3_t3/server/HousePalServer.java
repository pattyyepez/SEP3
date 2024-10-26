package en.via.sep3_t3.server;

import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.repositories.HouseOwnerRepository;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class HousePalServer {

  private HouseOwnerRepository houseOwnerRepository;

  public static void main(String[] args) throws Exception {
    new HousePalServer().run();
  }

  private void run() throws Exception {
    DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/postgres?currentSchema=housePal",
        "postgres",
        "ViaViaVia");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    houseOwnerRepository = new HouseOwnerRepository(jdbcTemplate);
//    HousePalServiceImpl housePalService = new HousePalServiceImpl(houseOwnerRepository);

    for (HouseOwner temp : houseOwnerRepository.findAll()){
      System.out.println(temp.getOwnerId() + " - " + temp.getAddress() + " - " + temp.getBiography());
    }

//    Server server = NettyServerBuilder.forPort(8080)
//        .addService(housePalService)
//        .build();

//    server.start();
//    System.out.println("HousePal.....");
//    server.awaitTermination();
  }
}
