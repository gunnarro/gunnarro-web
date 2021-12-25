package com.gunnarro.web.config;

import com.gunnarro.web.repository.UserAccountRepository;
import com.gunnarro.web.repository.impl.UserAccountRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * FIXME turned off for running on Azure
 *
 * @author admin
 */
@Slf4j
@Configuration
//@EnableTransactionManagement
public class DataSourceConfiguration {

    // If the same property is defined as a system property and in the properties
    // file, then the system property would be applied.
    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.user}")
    private String jdbcUser;

    @Value("${jdbc.pwd}")
    private String jdbcPwd;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClassName;

    @Primary
    @Bean
    public DataSource dietManagerDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(jdbcDriverClassName);
        dataSourceBuilder.url(jdbcUrl);
        dataSourceBuilder.username(jdbcUrl);
        dataSourceBuilder.password(jdbcPwd);

        log.info("jdbc url   : {}", jdbcUrl);
        log.info("jdbc user  : {}", jdbcUser);
        log.info("jdbc pwd   : {}", jdbcPwd.length());
        log.info("jdbc driver: {}", jdbcDriverClassName);
        log.info(System.getProperty("spring.config.location"));

        return dataSourceBuilder.build();
    }

    /**
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }
    */

    @Bean
    public UserAccountRepository userAccountRepository() {
        return new UserAccountRepositoryImpl(new JdbcTemplate(dietManagerDataSource()));
    }
}
