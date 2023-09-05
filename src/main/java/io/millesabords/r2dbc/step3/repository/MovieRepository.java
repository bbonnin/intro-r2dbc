package io.millesabords.r2dbc.step3.repository;

import io.millesabords.r2dbc.step3.entity.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MovieRepository extends ReactiveCrudRepository<Movie, Long> {

    Mono<Movie> findByTitle(String title);
}
