# intro-r2dbc

Code example on how to use R2DBC


## Demo

* Reactive Streams with Reactor: [IntroReactor.java](src/main/java/io/millesabords/r2dbc/reactor/IntroReactor.java)
* R2DBC, first examples: [IntroR2dbc.java](src/main/java/io/millesabords/r2dbc/base/IntroR2dbc.java)
* Spring Data R2DBC:
  * The configuration with the URL: [application.yml](src/main/resources/application.yml)
  * The entities, just [Robot.java](src/main/java/io/millesabords/r2dbc/entity/Robot.java)
  * DatabaseClient with [DatabaseClientExample](src/main/java/io/millesabords/r2dbc/DatabaseClientExample.java)
  * R2dbcEntityTemplate with [EntityTemplateExample](src/main/java/io/millesabords/r2dbc/EntityTemplateExample.java)
  * Reactive***Repository with [RepositoryExample](src/main/java/io/millesabords/r2dbc/RepositoryExample.java)
* R2DBC Proxy and Observability with [StatementExecListener](src/main/java/io/millesabords/r2dbc/StatementExecListener.java)

## TODO

* Entity Callback
* [WIP] Multi-tenancy: routing factory

