package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Movie;
import io.millesabords.r2dbc.entity.Robot;
import io.millesabords.r2dbc.entity.RobotWithMovie;
import io.millesabords.r2dbc.repository.MovieRepository;
import io.millesabords.r2dbc.repository.RobotRepository;
import io.millesabords.r2dbc.repository.RobotWithMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RepositoryExample {

    private final RobotRepository robotRepository;

    private final MovieRepository movieRepository;

    private final RobotWithMovieRepository robotWithMovieRepository;

    private final ReactiveTransactionManager transactionManager;

    @Transactional
    public Mono<Void> createRobotAndMovie(String name, String movie, String director) {
        return robotRepository.save(Robot.builder().name(name).movie(movie).build())
                .then(movieRepository.save(Movie.builder().title(movie).director(director).build()))
                .then();
    }

    public Mono<Void> createAgainRobotAndMovie(String name, String movie, String director) {
        TransactionalOperator rxtx = TransactionalOperator.create(transactionManager);

        return robotRepository.save(Robot.builder().name(name).movie(movie).build())
                //.as(rxtx::transactional)
                .then(movieRepository.save(Movie.builder().title(movie).director(director).build()))
                .then()
                .as(rxtx::transactional)
                ;
    }

    public Flux<Robot> findAll() {
        return robotRepository.findAll();
    }

    public Mono<Robot> createRobot(String name, String movie) {
        Robot newRobot = Robot.builder().name(name).movie(movie).build();
        return robotRepository.save(newRobot);
    }

    public Flux<String> getMovies() {
        return robotRepository.getMovies();
    }

    public Flux<String> getDirectors() {
        return robotRepository.getDirectors();
    }

    public Flux<RobotWithMovie> findRobotWithMovieByName(String name) {
        return robotWithMovieRepository.findByName(name);
    }
}
