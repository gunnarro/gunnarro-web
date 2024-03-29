package com.gunnarro.web.config;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * ref:
 * https://egkatzioura.com/2016/04/29/spring-boot-and-database-initialization/
 *
 * @author admin
 */
@Configuration
@EnableTransactionManagement
public class TestMariDBDataSourceConfiguration {
    @Bean
    public MariaDB4jSpringService mariaDB4jSpringService() {
        return new MariaDB4jSpringService();
    }

    @Bean
    public JdbcTemplate databaseJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public DataSource dietManagerDataSource(MariaDB4jSpringService mariaDB4jSpringService) throws ManagedProcessException {
        // Create our database with default root user and no password
        mariaDB4jSpringService.getDB().createDB("dietmanager-unittest");
        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();
        DataSource ds = DataSourceBuilder
                .create()
                .username("root")
                .password("")
                .url(config.getURL("dietmanager-unittest"))
                .driverClassName("org.mariadb.jdbc.Driver").build();
        try {
            DatabasePopulatorUtils.execute(createDatabasePopulator(), ds);
            return ds;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error init datasource: " + e.getMessage());
        }
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    private ResourceDatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource("schema.sql"));
        databasePopulator.addScript(new ClassPathResource("data.sql"));
        return databasePopulator;
    }

}
