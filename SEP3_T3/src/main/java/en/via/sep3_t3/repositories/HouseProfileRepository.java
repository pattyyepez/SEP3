package en.via.sep3_t3.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

public class HouseProfileRepository
{
  private static JdbcTemplate jdbcTemplate = null;

  public HouseProfileRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

}
