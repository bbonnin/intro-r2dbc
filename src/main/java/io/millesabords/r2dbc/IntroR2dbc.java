package io.millesabords.r2dbc;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import io.reactivex.rxjava3.core.Single;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class IntroR2dbc {

    public static void main(String[] args) throws IOException {
        init();

        //simpleQuery();
        complexQuery();
    }

    private static void complexQuery() {
        String name = "R2-D2";

        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///robot_db");

        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        Function<Connection, Publisher<? extends Result>> mapper = connection -> connection
                .createStatement("SELECT * FROM robot r, movie m WHERE r.movie = m.title AND r.name = $1")
                .bind("$1", name)
                .execute();

        Mono.from(connectionPublisher).log()
                .flatMapMany(mapper)
                .flatMap(result -> result.map((row, metadata) -> row.get("director", String.class)))
                .doOnNext(System.out::println)
                .subscribe();
    }

    private static void simpleQuery() {

        String name = "R2-D2";

        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///robot_db");

        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();

        Function<Connection, Publisher<? extends Result>> mapper = connection -> connection
                .createStatement("SELECT * FROM robot where name = $1")
                .bind("$1", name)
                .execute();

        Mono.from(connectionPublisher).log()
                .flatMapMany(mapper)
                .flatMap(result -> result.map((row, metadata) -> row.get("movie", String.class)))
                .doOnNext(System.out::println)
                .subscribe();

        /*Single.fromPublisher(connectionFactory.create()).toFlowable()
                .flatMap(connection -> connection
                        .createStatement("SELECT name FROM robot")
                        .execute())
                .flatMap(result -> result
                        .map((row, rowMetadata) -> row.get("name", String.class)))
                .doOnNext(System.out::println)
                .subscribe();*/
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
