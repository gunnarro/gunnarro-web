package com.gunnarro.followup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author mentos
 */
@SpringBootApplication
@ComponentScan("com.gunnarro.followup.*")
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOG.info("Start gunnarro:as web application ....");
        LOG.info(System.getProperty("spring.config.location"));
        LOG.info(System.getProperty("spring.profiles.active"));
        SpringApplication.run(Application.class, args);
    }
}
