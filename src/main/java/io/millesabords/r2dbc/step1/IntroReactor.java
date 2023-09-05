package io.millesabords.r2dbc.step1;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class IntroReactor {

    public static void main(String[] args) {
        //showFlux();
        //showFluxAlternative();
        //showFluxWithError();
        //showFluxWithErrorHandling();
        //showMono();
        showBackPressure();
        //showBackPressureWithStrategy();
    }

    private static void showFlux() {

        Flux<String> data = Flux.just("Hello", "How", "Are", "You?").log();

        data.subscribe(
                System.out::println,                                    // Consumer
                exc -> System.err.println("=== Aaargh !! " + exc),      // Error consumer
                () -> System.out.println("=== <Completed>")             // Complete consumer
        );

    }

    private static void showFluxAlternative() {

        Flux<String> data = Flux.just("Hello", "How", "Are", "You?").log();

        data
            .doOnNext(System.out::println)                                  // Consumer
            .doOnError((exc) -> System.err.println("=== Aaargh !! " + exc)) // Error consumer
            .doOnComplete(() -> System.out.println("=== <Completed>"))      // Complete consumer
            .subscribe();

    }

    private static void showFluxWithError() {

        Flux<String> data = Flux.just("Hello", "How", "Are").log()
                .concatWith(Flux.error(new Exception("BOOM !!!!")))
                .concatWith(Flux.just("You?"));                         // Never processed

        data.subscribe(
                System.out::println,                                    // Consumer
                (exc) -> System.err.println("=== Aaargh !! " + exc),    // Error consumer
                () -> System.out.println("=== <Completed>")             // Complete consumer
        );

    }

    private static void showFluxWithErrorHandling() {

        Flux<String> data = Flux.just("Hello", "How", "Are").log()
                .concatWith(Flux.error(new Exception("BOOM !!!!")))
                .concatWith(Flux.just("You?"))
                .onErrorResume((exc) -> {
                    System.err.println("=== Ignore this: " + exc);
                    return Flux.just("You?????????????");
                });

        data.subscribe(
                System.out::println,                                    // Consumer
                (exc) -> System.err.println("=== Aaargh !! " + exc),    // Error consumer
                () -> System.out.println("=== <Completed>")             // Complete consumer
        );

    }

    private static void showMono() {

        Mono<String> data = Mono.just("Hello").log();

        data
            .map(str -> str.repeat(3))
            .subscribe(
                System.out::println,                                    // Consumer
                (exc) -> System.err.println("=== Aaargh !! " + exc),    // Error consumer
                () -> System.out.println("=== <Completed>")             // Complete consumer
        );

    }

    private static void showBackPressure() {

        Flux<Integer> data = Flux.range(1, 10).log();

        data
                //.doOnRequest(r -> System.out.println("=== Request of " + r))    // Request consumer
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //super.hookOnSubscribe(subscription);
                        request(1); // Replaces the call to replace(unbound)
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("\t=== PROCESSING " + value);
                        if (value == 3) {
                            cancel();
                        } else {
                            request(1);
                        }
                    }
                });

    }

    public static void showBackPressureWithStrategy() {

        Flux.interval(Duration.ofMillis(1))//.log()
                .onBackpressureDrop()
                .concatMap(a -> Mono.delay(Duration.ofMillis(100)).thenReturn(a))
                .doOnNext(a -> System.out.println("=== Element kept by consumer: " + a))
                .blockLast();
    }
}
