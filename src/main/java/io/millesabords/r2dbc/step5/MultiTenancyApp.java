package io.millesabords.r2dbc.step5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = "io.millesabords.r2dbc.step5")
public class MultiTenancyApp {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenancyApp.class, args);
    }
}
