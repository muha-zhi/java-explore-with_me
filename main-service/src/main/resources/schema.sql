drop table if exists compilations_events;
drop table if exists compilations;
drop table if exists requests;
drop table if exists comments;
drop table if exists events;
drop table if exists locations;
drop table if exists category;
drop table if exists users;


CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(320),
    name  varchar(250),
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT unique_user_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS category
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       varchar(2000),
    CONSTRAINT unique_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat        DOUBLE PRECISION,
    lon        DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS compilations
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned       boolean,
    title        varchar(2000)
);

CREATE TABLE IF NOT EXISTS events
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation   varchar(2000),
    category_id  BIGINT,
    confirmed_requests BIGINT,
    description  varchar(7000),
    event_date    TIMESTAMP WITHOUT TIME ZONE,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT,
    location_id  BIGINT,
    paid         boolean,
    participant_limit BIGINT,
    published_on  TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean,
    state        varchar(20),
    title        varchar(2000),
    views        BIGINT,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT,
    event_id     BIGINT,
    created      TIMESTAMP WITHOUT TIME ZONE,
    status       varchar(20),
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT,
    compilation_id BIGINT,
    CONSTRAINT fk_compilations_events_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text         varchar(2000),
    event_id      BIGINT,
    author_id    BIGINT,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id)
);