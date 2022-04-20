package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Robot;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.sql.In;
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
                //.filter(statement -> statement.fetchSize(1))
                .map(row -> Robot.builder()
                        .id(row.get("id", Integer.class))
                        .name(row.get("name", String.class))
                        .movie(row.get("movie", String.class))
                        .build())
                .all();
    }
}
