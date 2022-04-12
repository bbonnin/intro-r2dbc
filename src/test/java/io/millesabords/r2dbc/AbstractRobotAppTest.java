package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Movie;
import io.millesabords.r2dbc.entity.Robot;
import io.millesabords.r2dbc.repository.MovieRepository;
import io.millesabords.r2dbc.repository.RobotRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.List;

public abstract class AbstractRobotAppTest {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @Before
    public void setup() {
        Hooks.onOperatorDebug();

        List<String> statements = List.of(
                "DROP TABLE IF EXISTS robot;",
                "DROP TABLE IF EXISTS movie;",
                "CREATE table robot (id INT AUTO_INCREMENT NOT NULL, name VARCHAR2, movie VARCHAR2);",
                "CREATE table movie (id INT AUTO_INCREMENT NOT NULL, title VARCHAR2, director VARCHAR2);");

        statements.forEach(it -> databaseClient.sql(it) //
                .fetch() //
                .rowsUpdated() //
                .as(StepVerifier::create) //
                .expectNextCount(1) //
                .verifyComplete());

        insertRobots();
    }

    private void insertRobots() {
        List<Robot> robots = List.of(
                new Robot(null, "R2-D2", "Star Wars 4"),
                new Robot(null, "BB-8", "Star Wars 7"),
                new Robot(null, "C-3PO", "Star Wars 4")
        );

        robotRepository.saveAll(robots).subscribe();

        List<Movie> movies = List.of(
                new Movie(null, "Star Wars 4", "George Lucas"),
                new Movie(null, "Star Wars 7", "J.J. Abrams")
        );

        movieRepository.saveAll(movies).subscribe();
    }
}
