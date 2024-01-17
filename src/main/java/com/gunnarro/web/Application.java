package com.gunnarro.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author mentos
 */
@Slf4j
//@SpringBootApplication
// disable datasource autoconfiguration
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan("com.gunnarro.web.*")
public class Application {

    public static void main(String[] args) {
        log.info("Start gunnarro:as web application ....");
        log.info("server.ssl.key-store: {}", System.getenv("GUNNARRO_KEYSTORE_PATH"));
        log.info("server.ssl.key-alias: {}", System.getenv("GUNNARRO_KEYSTORE_ALIAS"));

        Map<String, String> map = new TreeMap<>();
        log.info("===================================================");
        for(Package pkg: ClassLoader.getSystemClassLoader().getDefinedPackages()){
            if (ObjectUtils.allNotNull(pkg.getImplementationTitle(), pkg.getImplementationVersion())) {
                map.put(pkg.getImplementationTitle(), pkg.getImplementationVersion());
            }
        }
        map.forEach((key, value) -> log.info("application bom: vendor={}, version={}", key, value));
        SpringApplication.run(Application.class, args);
    }
}
