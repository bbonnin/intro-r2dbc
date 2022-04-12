package io.millesabords.r2dbc.base;

import io.millesabords.r2dbc.entity.Robot;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import io.reactivex.rxjava3.core.Single;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class IntroR2dbc {

    public static void main(String[] args) throws IOException {
        init();

        //simpleQueryWithReactor("R2-D2");
        //simpleQueryWithRx("R2-D2");
        //complexQuery("R2-D2");
        observe();
    }

    private static void observe() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                "r2dbc:proxy:h2:mem:///robot_db?proxyListener=io.millesabords.r2dbc.StatementExecListener");

        simpleQueryWithReactor("R2-D2", connectionFactory);
    }

    private static void complexQuery(String name) {
        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///robot_db");

        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        Function<Connection, Publisher<? extends Result>> mapper = connection -> connection
                .createStatement("SELECT * FROM robot r INNER JOIN movie m ON r.movie = m.title WHERE r.name = $1")
                .bind("$1", name)
                .execute();

        Mono.from(connectionPublisher).log()
                .flatMapMany(mapper)
                .flatMap(result -> result.map((row, metadata) -> row.get("director", String.class)))
                .doOnNext(System.out::println)
                .subscribe();
    }

    private static void simpleQueryWithReactor(String name) {
        simpleQueryWithReactor(name, ConnectionFactories.get("r2dbc:h2:mem:///robot_db"));
    }

    private static void simpleQueryWithReactor(String name, ConnectionFactory connectionFactory) {
        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        Function<Connection, Publisher<? extends Result>> statementMapper = connection -> connection
                //.createStatement("SELECT * FROM robot WHERE name = $1")
                .createStatement("SELECT * FROM robot")
                //.bind("$1", name)
                .execute();

        Mono<Connection> connection = Mono.from(connectionFactory.create());

        connection
                .flatMapMany(statementMapper)
                .flatMap(result -> result.map((row, metadata) -> Robot.builder().name(row.get("movie", String.class)).build()))
                .doOnNext(System.out::println)
                .doOnError(exc -> System.out.println("SNIF..." + exc))
                .doOnComplete(() -> System.out.println("FINI !"))
                .subscribe();
    }

    private static void simpleQueryWithRx(String name) {
        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///robot_db");

        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        Single.fromPublisher(connectionPublisher).toFlowable()
                .flatMap(connection -> connection
                        .createStatement("SELECT name FROM robot")
                        .execute())
                .flatMap(result -> result
                        .map((row, rowMetadata) -> row.get("name", String.class)))
                .doOnNext(System.out::println)
                .subscribe();
    }

    private static void init() throws IOException {
        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///robot_db");
        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        executeStatement("schema.sql", connectionPublisher);
        executeStatement("data.sql", connectionPublisher);
    }

    private static void executeStatement(String sqlFile, Publisher<? extends Connection> connectionPublisher)
            throws IOException {
        URL sqlResource = IntroR2dbc.class.getResource("/" + sqlFile);
        String query = IOUtils.toString(sqlResource, StandardCharsets.UTF_8);

        Mono.from(connectionPublisher)
                .flatMapMany(connection -> connection.createStatement(query).execute())
                .subscribe();
    }
}
