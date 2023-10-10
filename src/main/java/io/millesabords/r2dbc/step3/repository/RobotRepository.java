package io.millesabords.r2dbc.step3.repository;

import io.millesabords.r2dbc.step3.entity.Robot;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RobotRepository extends ReactiveCrudRepository<Robot, Long> {

    Flux<Robot> findByName(String name);

    //Mono<Robot> findByName2(String name);

    @Query("select name, movie from robot r where lower(r.movie) like concat('%', lower(:movie), '%')")
    Flux<Robot> findByMovie(String movie);

    @Query("select distinct movie from robot")
    Flux<String> getMovies();

    @Query("select distinct m.director from movie m inner join robot r on r.movie = m.title")
    Flux<String> getDirectors();
}
