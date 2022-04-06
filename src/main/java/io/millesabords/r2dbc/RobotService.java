package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Movie;
import io.millesabords.r2dbc.entity.Robot;
import io.millesabords.r2dbc.repository.MovieRepository;
import io.millesabords.r2dbc.repository.RobotRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final RobotRepository robotRepository;

    private final MovieRepository movieRepository;

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
                //.as(rxtx::transactional)
                ;
    }

    public Flux<Robot> findByNameUsingDBClient(String name) {

        return databaseClient
                .sql("SELECT * FROM robot WHERE name = :name")
                .bind("name", name)
                .map(row -> Robot.builder()
                        .name(row.get("name", String.class))
                        .movie(row.get("movie", String.class))
                        .build())
                .all();
    }

    public Flux<Robot> findByNameUsingTemplateV1(String name) {

        return r2dbcEntityTemplate.select(Robot.class)
                .from("robot")
                .matching(query(where("name").is(name)))
                .all();
    }

    public Flux<Robot> findByNameUsingTemplateV2(String name) {

        return r2dbcEntityTemplate.select(query(where("name").is(name)), Robot.class);
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
}
