package io.millesabords.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MovieRepository extends ReactiveCrudRepository<Movie, Long> {

    Mono<Movie> findByTitle(String title);
}
