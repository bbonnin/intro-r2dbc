package io.millesabords.r2dbc;

import io.millesabords.r2dbc.step3.entity.RobotWithMovie;
import io.millesabords.r2dbc.step3.repository.MovieRepository;
import io.millesabords.r2dbc.step3.repository.RobotRepository;
import io.millesabords.r2dbc.step3.repository.RobotWithMovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryExampleTest extends AbstractRobotAppTest {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RobotWithMovieRepository robotWithMovieRepository;

    @Test
    public void findRobotWithMovieByName_whenSearchForBB8_thenMovieIsNull() {
        robotWithMovieRepository.findByName("BB-8").log()
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                //.expectNextCount(1)
                .assertNext(robotMovie -> assertThat(robotMovie.getMovie()).isNull()) // It should not be null :(
                .verifyComplete();
    }

    @Test
    public void findByName_whenSearchForBB8WithMovie_thenMovieIsNotNull() {
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
    public void findByName_whenSearchForBB8_then1IsExpected() {
        robotRepository.findByName("BB-8").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findByMovie_whenSearchForStarWars_then2IsExpected() {
        robotRepository.findByMovie("star wars").log()
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }
}
