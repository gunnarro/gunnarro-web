package com.gunnarro.followup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mentos
 */
@Slf4j
@SpringBootApplication
@ComponentScan("com.gunnarro.followup.*")
public class Application {

    public static void main(String[] args) {
        log.info("Start gunnarro:as web application ....");
        log.info("server.ssl.key-store: {}", System.getenv("GUNNARRO_KEYSTORE_PATH"));
        log.info("server.ssl.key-alias: {}", System.getenv("GUNNARRO_KEYSTORE_ALIAS"));
        log.info("server.ssl.key-store: {}", System.getProperty("server.ssl.key-store"));
        SpringApplication.run(Application.class, args);
    }
}
