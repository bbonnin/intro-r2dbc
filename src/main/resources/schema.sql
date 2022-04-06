DROP TABLE IF EXISTS robot;

CREATE TABLE robot (
    id SERIAL,
    name VARCHAR,
    movie VARCHAR
);

DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
    id SERIAL,
    title VARCHAR,
    director VARCHAR
);
