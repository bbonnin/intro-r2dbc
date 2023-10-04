package io.millesabords.r2dbc.step6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LoomComparison {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        findR2D2WithFuture();
        findR2D2WithVirtualThreads();
    }

    record Robot(Integer id, String name) {}

    static void findR2D2WithFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> robotIdFuture = CompletableFuture.supplyAsync(() -> findRobotId("R2D2"));
        CompletableFuture<Robot> robotFuture = robotIdFuture.thenApply(robotId -> findRobotById(robotId));
        Robot robot = robotFuture.get();
        System.out.println(robot);
    }

    static Integer findRobotId(String name) {
        System.out.println("FIND ROBOT ID: " + Thread.currentThread());
        return 1;
    }

    static Robot findRobotById(Integer id) {
        System.out.println("FIND ROBOT BY ID: " + Thread.currentThread());
        return new Robot(id, "R2D2");
    }

    static void findR2D2WithVirtualThreads() throws InterruptedException {
        Thread.startVirtualThread(() -> {
            Integer robotId = findRobotId("R2D2");
            Robot robot = findRobotById(robotId);
            System.out.println(robot);
        }).join();
    }

}
