package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmDao filmStorage;
    private final UserDao userDao;
    private final FilmGenreDao filmGenreDao;
    private final LikeDao likeDao;
    private final GenresDao genresDao;

    @Autowired
    public FilmService(@Qualifier("filmJdbcDao") FilmDao filmStorage,
                       @Qualifier("userJdbcDao") UserDao userDao,
                       LikeDao likeDao,
                       FilmGenreDao filmGenreDao,
                       GenresDao genresDao) {
        this.filmStorage = filmStorage;
        this.userDao = userDao;
        this.likeDao = likeDao;
        this.filmGenreDao = filmGenreDao;
        this.genresDao = genresDao;
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        for (Film film : films) {
            fillGenre(film);
        }
        return films;
    }

    public Film save(Film film) throws ValidationException {
        Film created = filmStorage.save(film);
        if (Objects.nonNull(film.getGenres())) {
            //delete all old link create
            List<FilmGenre> filmGenres = filmGenreDao.findAll(created);
            filmGenres.forEach(filmGenre -> filmGenreDao.delete(filmGenre.getFilmId(), filmGenre.getGenreId()));

            //create new
            film.getGenres().forEach(genre -> filmGenreDao.save(FilmGenre.builder()
                    .genreId(genre.getId())
                    .filmId(created.getId())
                    .build()));
        }

        return fillGenre(created);
    }

    public Film update (Film film) {
        checkFilmId(film.getId());
        Film updated = filmStorage.update(film);

        if (Objects.nonNull(film.getGenres())) {
            //delete all old link create
            List<FilmGenre> filmGenres = filmGenreDao.findAll(updated);
            filmGenres.forEach(filmGenre -> filmGenreDao.delete(filmGenre.getFilmId(), filmGenre.getGenreId()));

            //create new
            film.getGenres().forEach(genre -> filmGenreDao.save(FilmGenre.builder()
                    .genreId(genre.getId())
                    .filmId(updated.getId())
                    .build()));
        }

        return fillGenre(updated);
    }


    public void deleteFilm (Long id) throws FilmNotFoundException {
        filmStorage.deleteFilm(id);
    }


    public Film findFilmById(Long id) throws FilmNotFoundException {
        checkFilmId(id);
        log.info("Попытка получить фильм по id {}",
                id);

        return fillGenre(filmStorage.findFilmById(id));
    }

    public void addLikeToFilm(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        log.info("Пользователь с id {} хочет поставить лайк фильму с id {}", userId, filmId);

        if (userId < 0) {
            throw new UserNotFoundException("User not found with id " + userId);
        }

        Film film = findFilmById(filmId);
        User user = userDao.findUserById(userId);
        likeDao.addLikeToFilm(film.getId(), user.getId());

        log.info("Пользователь с id {} поставил лайк фильму с id {}", user.getId(), film.getId());
    }

    public String deleteLikeToFilm(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        log.info("Пользователь с id {} хочет удалить лайк с фильма с id {}", userId, filmId);

        if (userId < 0) {
            throw new UserNotFoundException("User not found with id " + userId);
        }

        Film film = findFilmById(filmId);
        User user = userDao.findUserById(userId);
        likeDao.deleteLikeToFilm(film.getId(), user.getId());

        log.info("Пользователь с id {} удалил лайк с фильма с id {}", userId, filmId);
        return "Лайк удален";
    }

    public Collection<Film> getPopularFilms(Integer count) {
        log.info("Пользователь запросил список популярных фильмов");

        List<Film> films = filmStorage.findPopularFilm(count);
        log.info("Передан список популярных фильмов");
        return films;
    }

    private void checkFilmId (Long id) throws FilmNotFoundException {
        if (id < 1L) {
            throw new FilmNotFoundException("Фильм не может быть обновлен или найден," +
                    "так как id не может быть меньше 1 ничему не равняться");
        }
        log.info("Фильм не может быть обновлен или найден," +
                "так как id не может быть меньше 1 или ничему не равняться");
    }

    private Film fillGenre(Film film) {
        List<Genre> genres = filmGenreDao.findAll(film)
                .stream()
                .map(FilmGenre::getGenreId)
                .map(genresDao::findGenreById)
                .collect(Collectors.toList());

        film.setGenres(genres);
        return film;
    }
}
