package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Movie;
import io.millesabords.r2dbc.entity.Robot;
import io.millesabords.r2dbc.entity.RobotWithMovie;
import io.millesabords.r2dbc.repository.MovieRepository;
import io.millesabords.r2dbc.repository.RobotWithMovieRepository;
import io.millesabords.r2dbc.repository.RobotRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RobotAppTest {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RobotWithMovieRepository robotWithMovieRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RobotService robotService;

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

    @Test
    public void whenSearchForBB8Movie_then1IsExpected() {
        robotWithMovieRepository.findByName("BB-8").log()
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                //.expectNextCount(1)
                .assertNext(robotMovie -> assertThat(robotMovie.getMovie()).isNotNull())
                .verifyComplete();
    }

    @Test
    public void whenSearchForBB8WithMovie_then1IsExpected() {
        robotRepository.findByName("BB-8").log()
                .flatMap(robot -> movieRepository
                        .findByTitle(robot.getMovie()).log()
                        .flatMap(movie -> Mono.just(new RobotWithMovie(robot.getId(), robot.getName(), movie))))
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .assertNext(robotMovie -> assertThat(robotMovie.getMovie()).isNotNull())
                .verifyComplete();
    }

    @Test
    public void whenSearchForBB8_then1IsExpected() {
        robotRepository.findByName("BB-8").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void whenSearchForStarWars_then2IsExpected() {
        robotRepository.findByMovie("star wars").log()
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void whenSearchForR2D2_then1IsExpected() {
        robotService.findByNameUsingDBClient("R2-D2").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void whenSearchForC3PO_then1IsExpected() {
        robotService.findByNameUsingTemplateV1("C-3PO").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        robotService.findByNameUsingTemplateV2("C-3PO").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
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
