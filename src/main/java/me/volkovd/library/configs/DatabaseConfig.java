package me.volkovd.library.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${hibernate.driver_class}")
    private String DRIVER_CLASS;

    @Value("${hibernate.connection.url}")
    private String URL;

    @Value("${hibernate.connection.username}")
    private String USERNAME;

    @Value("${hibernate.connection.password}")
    private String PASSWORD;

    private final Environment env;

    @Autowired
    public DatabaseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(DRIVER_CLASS);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

}
