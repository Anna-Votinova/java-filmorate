package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

@AllArgsConstructor
@Component
public class FilmGenreService {

    private final FilmGenreDao filmGenreDao;

    public FilmGenre save(FilmGenre filmGenre) {
        return filmGenreDao.save(filmGenre);
    }

    public void delete(long filmId, long genreId) {
        filmGenreDao.delete(filmId, genreId);
    }

    public List<FilmGenre> findAll(Film film) {
        return filmGenreDao.findAll(film);
    }

}
