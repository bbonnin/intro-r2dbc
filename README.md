# intro-r2dbc
Code example on how to use R2DBC

## Links

https://docs.spring.io/spring-boot/docs/2.4.x/reference/htmlsingle/#howto-initialize-a-database-using-r2dbc
> Indique qu'il faut quand même inclure spring-jdbc pour utiliser FLyway ou Liquibase
> Cette remarque n'apparait plus dans la doc Spring Boot 2.6.x

##  Slides

Introduction

R2DBC
* Design principles
  * embrace reactive types and patterns - adopter des types et des modèles réactifs
  * non-blocking, all the way to the database - non bloquant, jusqu'à la base de données
  * shrink the driver SPI (service provider interface)
  * enable multiple "humane" APIs 
* Driver SPI: ConnectionFactory
  * Publisher<Connection> create()
  * ConnectionFactoryMetadata getMetadate()
* Driver SPI: Connection
  * Publisher<Void> ...
  * operates with transactions
  * for developpers:
    * createBatch
    * createStatement
  
Spring Data R2DBC
```java
DatabaseClient client = DatabaseClient.create(connectionFactory);

Flux<Robot> rows = client
        .execute("SELECT * FROM robot WHERE name = :name")
        .bind("name", "r2d2")
        .as(Robot.class)
        .fetch()
        .all();
```
* Fluent API
* Named parameters
* How to consume the data (fetching all rows)
* DatabaseClient est proche d'un jdbctemlate
* And existing ReactiveXXXRepository
  * ideal si vous êtes habitué aux repository JPA

```java
import io.millesabords.r2dbc.entity.Robot;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface RobotRepository extends ReactiveCrudRepository<Robot, Long> {
  @Query("SELECT * FROM robot WHERE name = :name")
  Flux<Robot> findByName(String name);
} 

repository.findByName("r2d2")
        .doOnEach(r->System.out.println(r.name));
```
* Very functional API
* Support transactions
* named parameters support

Demo
* Intellij
* Contenu du pom
  * start data r2dbc
  * driver de la base
* le pojo de Robot
* un repository
* un controller pour la demo
* lancement du programme
* avec le repository.findall : on y accède avec curl/postman
* avec le repository.findbyname: idem
  * avec un bon et un mauvais nom
* avec un databaseclient (aucune différence)
  * juste plus flexible, ça dépend de vos besoins
* creation d'un nouveau record
  * repostiory.save().then() // notify me quand c'est fait
  * tracking ce qu'il se passe avec un event
  * que se passe-t-il si le 2eme save se plante ?
    * le scope de la transaction fait que le 1er save sera réalisé mais pas le second
* Atomic operations
  * on ne peut pas se reposer sur le thread local storage dans un contexte reactive, ce qui est la base pour les transactions
* Reactive transactions
  * Bind transactional state to Subscription
    * Reactor context
  * Dans spring: ReactiveTransactionManager

Demo
* Meme code avec l'annotation @TRansactional

```java
@Transactional
Mono<Void> create(){
    return repository1.save(data1)
        .then(repository2.save(data2))
        .then();
}

// Alternative avec un TransactionalOperator rxtx
rxtx.execute(status -> {
    return repository1.save(data1)
        .then(repository2.save(data2));
});

// OU
    return repository1.save(data1)
        .then(repository2.save(data2))
        .then()
        .as(rxtx::transactional); // affecte les opérations AVANT, pas ce qui est après 
``` 

* Avertissements sur les transactions
  * requiert le Reactor Context (à vérifier si c'est toujours vrai)
  * avoid cancellation of transactional stream (Flux.next, Flux.take) to retain synchronization

* Connection URL
  * pour jdbc, chaque fournisseur de driver fait ce qu'il veut apres le "jdbc:"

* Spring boot starter
  * fournit ce qu'il faut en termes de configuration pour démarrer avec R2DBC
  * actuator integration
  * @DataR2dbcTest

* Eco-system R2DBC
  * Drivers
  * client implementations
    * Spring data Rédbc
    * r2dbc-client
    * Kotysa (Kotlin)
  * Spec docs

* Il existe d'autres drivers et projets autour du non-blocking
  * Jasync, Vert.x
  * Hibernate Rx
  * OJDBC 20 with Reactive extensions (Oracle)

* Roadmap
  * started as experiment in early 2018
  * Proved to work
  * there is demand
  * A venir (2019) : stored procedure, extended transactions spec, EventProvider

* Specific to Postgresl
  * LISTEN
```java
this.connection.createStatement("LISTEN login_event_notification")
        .execute()
        .flatMap(PostgresqlResult::getRowsUpdated)
        .subscribe();
```

```java
PostgresqlConnection connection; // Existence dans le driver JDBC ?

@GetMapping(value = "/login-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
Flux<CharSequence> getStream() {
    Flux<Notification> notifications = connection.getNotifications(); // Methode existante dans driver JDBC
    return notifications.map(Notification::getParameter);
}

// Basé sur des triggers créé dans la base
```

* R2DBC Clients
  * MyBatis
  * JDBI
  * jOOQ
  * Querydsl
  * Helidon
  * Liquibase
  * Flyway
  * Exposed
  * Testcontainers

website: r2dbc.io
twitter: @r2dbc
source: github.com/r2dbc


JPA est construit au dessus de JDBC qi est bloquant