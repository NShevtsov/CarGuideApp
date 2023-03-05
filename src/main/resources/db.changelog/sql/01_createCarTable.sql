

CREATE TABLE car
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    brand           VARCHAR(30) NOT NULL,
    model           VARCHAR(30) NOT NULL,
    color           VARCHAR(30) NOT NULL,
    registration_number VARCHAR(10) NOT NULL UNIQUE,
    production_year INT         NOT NULL,
    creation_time   timestamptz NOT NULL,
    update_time     timestamptz NOT NULL
);
