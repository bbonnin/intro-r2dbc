package io.millesabords.r2dbc.step3;

import io.millesabords.r2dbc.step3.entity.Robot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RobotController {

    private final Example3ReactiveCrudRepository repositoryExample;

    @GetMapping(value = "/robot-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Robot> getRobotStream() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(repositoryExample.findAll(), interval, (robot, time) -> robot);
        //return repositoryExample.findAll();
    }

    @GetMapping("/robots")
    public Flux<Robot> getAllRobots() {
        return repositoryExample.findAll();
    }

    @PostMapping("/robot")
    public Mono<Robot> create(@RequestParam String name, @RequestParam String movie) {
        return repositoryExample.createRobot(name, movie);
    }

    @PostMapping("/robot-movie")
    public Mono<Void> create(@RequestParam String name, @RequestParam String movie,
                              @RequestParam String director) {
        return repositoryExample.createRobotAndMovie(name, movie, director);
    }

    @GetMapping("/movies")
    public Mono<List<String>> getAllMovies() {
        return repositoryExample.getMovies().collectList();
    }

    @GetMapping("/directors")
    public Mono<List<String>> getAllDirectors() {
        return repositoryExample.getDirectors().collectList();
    }

}
