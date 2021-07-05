package com.gunnarro.followup.config;

/*
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
*/
//@Configuration
public class EmbeddedMariaDbConfig {
  /**
    @Bean
    public MariaDB4jSpringService mariaDB4jSpringService() {
        return new MariaDB4jSpringService();
    }

    @Bean
    public JdbcTemplate dietManagerJdbcTemplate(DataSource dataSource) {
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
      //  databasePopulator.addScript(new ClassPathResource("schema.sql"));
      //  databasePopulator.addScript(new ClassPathResource("data.sql"));
        return databasePopulator;
    }
    */
}
