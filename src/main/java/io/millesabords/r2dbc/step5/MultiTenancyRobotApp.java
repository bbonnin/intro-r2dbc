package io.millesabords.r2dbc.step5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(exclude = R2dbcAutoConfiguration.class)
@Profile("multi-tenancy")
public class MultiTenancyRobotApp {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenancyRobotApp.class, args);
    }
}
