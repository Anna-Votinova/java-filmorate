CREATE TABLE IF NOT EXISTS mpa_rating (
    id int generated by default as identity primary key,
    name varchar(10) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films
(
    id long generated by default as identity primary key,
    name varchar(100) NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    rate integer NOT NULL,
    mpa_rating integer REFERENCES mpa_rating (id) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS genres
(
    id int generated by default as identity primary key,
    genre_name varchar(15) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films_genres
(
    id int generated by default as identity primary key,
    genre_id int REFERENCES genres (id) ON DELETE RESTRICT,
    film_id long REFERENCES films (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS users
(
    id long generated by default as identity primary key,
    email varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    login varchar(50) NOT NULL,
    birthday date NOT NULL
    );

CREATE TABLE IF NOT EXISTS friends
(
    id long generated by default as identity primary key,
    user_id long REFERENCES users (id) ON DELETE CASCADE ,
    friend_id long REFERENCES users (id) ON DELETE CASCADE,
    is_friend boolean
    );

CREATE TABLE IF NOT EXISTS likes
(
    id long generated by default as identity primary key,
    films_id long REFERENCES films (id) ON DELETE CASCADE,
    user_id long REFERENCES users (id) ON DELETE CASCADE
    );
