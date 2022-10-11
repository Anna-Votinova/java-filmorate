package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    List<Film> findAll();

    List<Film> findPopularFilm(Integer limit);
    Film save(Film film) throws ValidationException;
    Film update (Film film) throws FilmNotFoundException;
    Film findFilmById(Long id) throws FilmNotFoundException;
    void deleteFilm (Long id) throws FilmNotFoundException;
}
