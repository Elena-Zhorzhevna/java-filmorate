DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS rating_mpa CASCADE;
DROP TABLE IF EXISTS friendship_status CASCADE;

CREATE TABLE rating_mpa (
  rating_id INTEGER NOT NULL UNIQUE,
  name varchar(120) NOT NULL,
  description varchar(255) NOT NULL,
  PRIMARY KEY(rating_id)
);

CREATE TABLE friendship_status (
  status_id INTEGER NOT NULL UNIQUE,
  status_name varchar(120) NOT NULL,
  PRIMARY KEY(status_id)
);

CREATE TABLE genre (
  genre_id INTEGER NOT NULL UNIQUE,
  name varchar(120) NOT NULL,
  PRIMARY KEY(genre_id)
);

CREATE TABLE film_genre (
  film_id BIGINT NOT NULL UNIQUE,
  genre_id INTEGER NOT NULL UNIQUE,
  PRIMARY KEY(film_id),
  FOREIGN KEY (genre_id) REFERENCES genre(genre_id)
);

CREATE TABLE films (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE ,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    rating_id integer,
    FOREIGN KEY (rating_id) REFERENCES rating_mpa (rating_id)
);

CREATE TABLE users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE ,
    login varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    birthday date
);

CREATE TABLE film_likes (
    film_id BIGINT REFERENCES films (film_id),
    user_id BIGINT REFERENCES users (user_id),
    PRIMARY KEY(film_id, user_id)
);

CREATE TABLE friends (
 user_id BIGINT NOT NULL,
 friend_id BIGINT NOT NULL UNIQUE,
 status_id INTEGER NOT NULL UNIQUE ,
 PRIMARY KEY(user_id, friend_id),
 FOREIGN KEY (user_id) REFERENCES users(user_id),
 FOREIGN KEY (friend_id) REFERENCES users(user_id),
 FOREIGN KEY (status_id) REFERENCES friendship_status(status_id)
);