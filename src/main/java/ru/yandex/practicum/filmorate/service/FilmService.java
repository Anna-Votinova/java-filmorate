package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilmById(Long id) throws FilmNotFoundException {
        log.info("Попытка получить фильм по id {}",
                id);
        return filmStorage.findFilmById(id);
    }

    public void addLikesToSetOfFilm (Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        log.info("Пользователь с id {} хочет поставить лайк фильму с id {}", userId, filmId);
        Film film = findFilmById(filmId);
        Set<Long> filmL = checkNullSet(film.getLikes());
        filmL.add(userStorage.findUserById(userId).getId());
        film.setLikes(filmL);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        filmLikes.put(filmId, filmStorage.findFilmById(filmId).getLikes());
    }

    public String deleteLikesToSetOfFilm (Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        log.info("Пользователь с id {} хочет удалить лайк с фильма с id {}", userId, filmId);
        findFilmById(filmId).getLikes().remove(userStorage.findUserById(userId).getId());
        log.info("Пользователь с id {} удалил лайк с фильма с id {}", userId, filmId);
        filmLikes.put(filmId, filmStorage.findFilmById(filmId).getLikes());
        return "Лайк удален";
    }

    public Collection<Film> getPopularFilms(Integer count) {
        log.info("Пользователь запросил список популярных фильмов");
        final Comparator<Film> comparator = Comparator
                .comparingInt(o -> filmLikes.getOrDefault(o.getId(), new HashSet<>()).size());
        log.info("Передан список популярных фильмов");
        return filmStorage.findAll().stream().sorted(comparator.reversed())
                .limit(count).collect(Collectors.toList());
    }

    private Set<Long> checkNullSet(Set<Long> set) {
        if (Objects.isNull(set)) {
            return new HashSet<>();
        }
        return set;
    }
}
