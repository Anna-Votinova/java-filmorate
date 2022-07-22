package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {
    Set<Film> findAll();
    Film create(Film film) throws ValidationException;
    Film update (Film film) throws FilmNotFoundException;
    Film findFilmById(Long id) throws FilmNotFoundException;
}
