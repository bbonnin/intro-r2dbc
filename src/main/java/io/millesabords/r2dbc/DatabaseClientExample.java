package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Robot;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class DatabaseClientExample {

    private final DatabaseClient databaseClient;

    public Flux<Robot> findRobotsByName(String name) {

        return databaseClient
                .sql("SELECT * FROM robot WHERE name = :name")
                .bind("name", name)
                .map(row -> Robot.builder()
                        .name(row.get("name", String.class))
                        .movie(row.get("movie", String.class))
                        .build())
                .all();
    }
}
