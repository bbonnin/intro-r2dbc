package io.millesabords.r2dbc;

import lombok.RequiredArgsConstructor;
//import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

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
}
