package io.millesabords.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RobotMovieRepository extends ReactiveCrudRepository<RobotMovie, Long> {

    Flux<RobotMovie> findByName(String name);
}
