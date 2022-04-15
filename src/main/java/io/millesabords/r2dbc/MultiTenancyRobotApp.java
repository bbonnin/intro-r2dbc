package io.millesabords.r2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = R2dbcAutoConfiguration.class)
public class MultiTenancyRobotApp {

    static {
        System.setProperty("spring.profiles.active", "multi-tenancy"); // Hem...
    }

    public static void main(String[] args) {
        SpringApplication.run(MultiTenancyRobotApp.class, args);
    }
}
