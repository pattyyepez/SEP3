package en.via.sep3_t3.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up the data source and JDBC template.
 * This class provides the necessary beans to interact with a PostgresSQL database.
 */
@Configuration
public class DataSourceConfig {

  /**
   * Configures and provides a {@link DataSource} bean.
   *
   * <p>The data source connects to a PostgresSQL database located at the specified URL,
   * using the provided username and password.</p>
   *
   * @return a configured {@link DataSource} instance.
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres?currentSchema=housepal");
    dataSource.setUsername("postgres");
    dataSource.setPassword("ViaViaVia");
    return dataSource;
  }

  /**
   * Configures and provides a {@link JdbcTemplate} bean.
   *
   * <p>This bean is used to simplify interactions with the database by providing
   * methods for executing SQL queries and updates.</p>
   *
   * @param dataSource the {@link DataSource} to use for database connections.
   * @return a configured {@link JdbcTemplate} instance.
   */
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}

