package io.millesabords.r2dbc;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RobotController {

    private final RobotRepository robotRepository;

    @GetMapping(value = "/robot-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Robot> getRobotStream() {
        //Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        //return Flux.zip(robotRepository.findAll(), interval, (robot, time) -> robot);
        return robotRepository.findAll();
    }

    @GetMapping("/robot")
    public Flux<Robot> getAllRobots() {
        return robotRepository.findAll();
    }

    @PostMapping("/robot")
    public Mono<Robot> create(@RequestParam String name, @RequestParam String movie) {
        Robot newRobot = Robot.builder().name(name).movie(movie).build();
        return robotRepository.save(newRobot);
    }

    @GetMapping("/movies")
    public Mono<List<String>> getAllMovies() {
        return robotRepository.getMovies().collectList();
    }

    @GetMapping("/directors")
    public Mono<List<String>> getAllDirectors() {
        return robotRepository.getDirectors().collectList();
    }

}
