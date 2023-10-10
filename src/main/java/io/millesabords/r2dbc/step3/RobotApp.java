package io.millesabords.r2dbc.step3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.millesabords.r2dbc.step3")
//@EnableR2dbcRepositories
public class RobotApp {

    public static void main(String[] args) {
        SpringApplication.run(RobotApp.class, args);
    }
}
