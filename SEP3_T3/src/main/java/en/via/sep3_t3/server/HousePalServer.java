package en.via.sep3_t3.server;

import en.via.sep3_t3.domain.HouseOwner;
import en.via.sep3_t3.domain.HouseSitter;
import en.via.sep3_t3.repositories.HouseOwnerRepository;
import en.via.sep3_t3.repositories.HouseProfileRepository;
import en.via.sep3_t3.repositories.HouseSitterRepository;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class HousePalServer {

  private HouseOwnerRepository houseOwnerRepository;
  private HouseSitterRepository houseSitterRepository;
  private HouseProfileRepository houseProfileRepository;

  public static void main(String[] args) throws Exception {
    new HousePalServer().run();
  }

  private void run() throws Exception {
    DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/postgres?currentSchema=housePal",
        "postgres",
        "ViaViaVia");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    houseOwnerRepository = new HouseOwnerRepository(jdbcTemplate);
    houseSitterRepository = new HouseSitterRepository(jdbcTemplate);
    houseProfileRepository = new HouseProfileRepository(jdbcTemplate);
    HouseOwnerServiceImpl houseOwnerService = new HouseOwnerServiceImpl(houseOwnerRepository);
    HouseSitterServiceImpl houseSitterService = new HouseSitterServiceImpl(houseSitterRepository);
    HouseProfileServiceImpl houseProfileService = new HouseProfileServiceImpl(houseProfileRepository);
//    HouseOwnerServiceImpl houseOwnerService = new HouseOwnerServiceImpl(houseOwnerRepository);

    for (HouseSitter temp : houseSitterRepository.findAll()){
      System.out.println(temp);
    }
    HouseSitter sitter = houseSitterRepository.findById(5);
    sitter.setBiography("pooping");
    sitter.setEmail("pooper@gmail.com");
    houseSitterRepository.save(sitter);

    for (HouseSitter temp : houseSitterRepository.findAll()){
      System.out.println(temp);
    }

    Server server = NettyServerBuilder.forPort(9090)
        .addService(houseOwnerService)
        .addService(houseSitterService)
        .addService(houseProfileService)
        .build();

    server.start();
    System.out.println("HousePal.....");
    server.awaitTermination();
  }
}
