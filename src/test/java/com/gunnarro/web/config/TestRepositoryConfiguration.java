package com.gunnarro.web.config;

import com.gunnarro.web.repository.ActivityRepository;
import com.gunnarro.web.repository.LogEventRepository;
import com.gunnarro.web.repository.UserAccountRepository;
import com.gunnarro.web.repository.impl.ActivityRepositoryImpl;
import com.gunnarro.web.repository.impl.LogEventRepositoryImpl;
import com.gunnarro.web.repository.impl.UserAccountRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class TestRepositoryConfiguration {


    @Autowired
    @Qualifier(value = "dietManagerDataSource")
    private DataSource dataSource;

    @Bean
    public LogEventRepository logEventRepository() {
        return new LogEventRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Bean
    public ActivityRepository activityRepository() {
        return new ActivityRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Bean
    public UserAccountRepository userAccountRepository() {
        return new UserAccountRepositoryImpl(new JdbcTemplate(dataSource));
    }

}
