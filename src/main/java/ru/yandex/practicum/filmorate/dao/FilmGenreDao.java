package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreDao {

    FilmGenre save(FilmGenre filmGenre);
    void delete(long filmId, long genreId);

    FilmGenre findById(long id);

    List<FilmGenre> findAll(Film film);
}
