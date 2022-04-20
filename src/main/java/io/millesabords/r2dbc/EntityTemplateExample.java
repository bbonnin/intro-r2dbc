package io.millesabords.r2dbc;

import io.millesabords.r2dbc.entity.Robot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class EntityTemplateExample {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Flux<Robot> findRobotsByNameV1(String name) {

        return r2dbcEntityTemplate
                .select(Robot.class)
                .from("robot")
                .matching(query(where("name").is(name)))
                .all();
    }

    public Flux<Robot> findRobotsByNameV2(String name) {

        return r2dbcEntityTemplate
                .select(query(where("name").is(name)), Robot.class);
    }

    public Mono<Robot> createRobot(Robot robot) {
        return r2dbcEntityTemplate.insert(robot);
    }

    public Mono<Integer> deleteRobotsOfMovie(String movie) {
        return r2dbcEntityTemplate
                .delete(Robot.class)
                .matching(query(where("movie").like("%" + movie + "%")))
                .all();
    }
}
