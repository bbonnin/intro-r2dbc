package io.millesabords.r2dbc.repository;

import io.millesabords.r2dbc.entity.RobotWithMovie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RobotWithMovieRepository extends ReactiveCrudRepository<RobotWithMovie, Long> {

    Flux<RobotWithMovie> findByName(String name);
}
