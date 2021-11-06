package com.gunnarro.followup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author mentos
 */
@SpringBootApplication
@ComponentScan("com.gunnarro.followup.*")
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOG.info("Start gunnarro:as web application ....");
        LOG.info("server.ssl.key-store: {}", System.getenv("GUNNARRO_KEYSTORE_PATH"));
        LOG.info("server.ssl.key-alias: {}", System.getenv("GUNNARRO_KEYSTORE_ALIAS"));
        LOG.info("server.ssl.key-store: {}", System.getProperty("server.ssl.key-store"));

        /*
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            if (envName.startsWith("GUNNARRO"))
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }
         */
        SpringApplication.run(Application.class, args);
    }
}
