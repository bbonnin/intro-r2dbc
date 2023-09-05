# intro-r2dbc

Code examples on how to use R2DBC, and Spring Data R2DBC.


## Demo

The examples have been splitted in several steps:
* Step #1: Reactive Streams with Reactor: [IntroReactor.java](src/main/java/io/millesabords/r2dbc/step1/IntroReactor.java)
* Step #2: R2DBC, first examples: [IntroR2dbc.java](src/main/java/io/millesabords/r2dbc/step2/IntroR2dbc.java)
* Step #3: Spring Data R2DBC:
  * Common: The configuration with the URL: [application.yml](src/main/resources/application.yml)
  * Common: The entities, just [Robot.java](src/main/java/io/millesabords/r2dbc/step3/entity/Robot.java)
  * Example #1: DatabaseClient with [DatabaseClientExample](src/main/java/io/millesabords/r2dbc/step3/Example1DatabaseClient.java)
  * Example #2: R2dbcEntityTemplate with [EntityTemplateExample](src/main/java/io/millesabords/r2dbc/step3/Example2R2dbcEntityTemplate.java)
  * Example #3: Reactive***Repository with [RepositoryExample](src/main/java/io/millesabords/r2dbc/step3/Example3ReactiveCrudRepository.java)
* Step #4: R2DBC Proxy and Observability with [StatementExecListener](src/main/java/io/millesabords/r2dbc/step4/StatementExecListener.java)
* Step #5: Multi-tenancy (TO BE COMPLETED)
* Step #6: Query example
* Step #7: Entity callback
* Step #8: jOOQ ?


## TODO

* [WIP] Multi-tenancy: routing factory
* Entity Callback
* Query by example
* What else ?


## Some links

* About jOOQ and Reactive fetching: https://www.jooq.org/doc/latest/manual/sql-execution/fetching/reactive-fetching/