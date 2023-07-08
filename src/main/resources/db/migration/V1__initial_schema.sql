DROP TABLE IF EXISTS game;
CREATE TABLE game
(
    id              BIGSERIAL PRIMARY KEY   NOT NULL,
    game_id         varchar(63) UNIQUE      NOT NULL,
    title           varchar(255)            NOT NULL,
    genre           varchar(31)             NOT NULL,
    publisher       varchar(127)            NOT NULL,
    price           float8                  NOT NULL,
    created         time                    NOT NULL,
    last_modified   time                    NOT NULL,
    version         integer                 NOT NULL
);
