package io.millesabords.r2dbc.step6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class LoomComparison {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LoomComparison comparison = new LoomComparison();
        System.out.println("----");
        comparison.findR2D2WithFuture();
        System.out.println("----");
        comparison.findR2D2WithVirtualThreads();
        System.out.println("----");
    }

    private final StarWarsRepository repository;

    public LoomComparison() {
        repository = new StarWarsRepository();
    }

    private void findR2D2WithFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Robot> robotFuture = CompletableFuture
                .supplyAsync(() -> repository.findRobotByName("R2D2"));
        CompletableFuture<Movie> movieFuture = robotFuture
                .thenApplyAsync(robot -> repository.findMovieById(robot.firstMovie));
        Movie movie = movieFuture.get();
        System.out.println("First film with R2D2: " + movie.title);
    }

    private void findR2D2WithVirtualThreads() throws InterruptedException {
        Thread.startVirtualThread(() -> {
            Robot robot = repository.findRobotByName("R2D2");
            Movie movie  = repository.findMovieById(robot.firstMovie);
            System.out.println("First film with R2D2: " + movie.title);
        }).join();
    }

    record Robot(Integer id, String name, Integer firstMovie) {}

    record Movie(Integer id, String title) {}

    static class StarWarsRepository {

        public Robot findRobotByName(String name) {
            System.out.printf("%s - FIND ROBOT WITH THE NAME %s%n", Thread.currentThread(), name);
            return new Robot(1, name, 4);
        }

        public Movie findMovieById(Integer id) {
            System.out.printf("%s - FIND MOVIE WITH ID %s%n", Thread.currentThread(), id);
            return new Movie(id, "Star Wars, episode " + id);
        }
    }

}
