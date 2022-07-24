package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long idCounter = 1L;
    private Set<Film> films = new HashSet<>();

    private void generateId(Film film) {
        if (film.getId() == null) {
            log.debug("Сгенерирован новый id");
            film.setId(idCounter++);
        }
    }

    @Override
    public Set<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @Override
    public Film create(Film film) throws ValidationException {
        log.info("Будет создан фильм {}", film.toString());
        findFilmInSet(film);
        generateId(film);
        log.debug("Фильм помещен в таблицу");
        films.add(film);
        return film;
    }

    @Override
    public Film update(Film film) throws FilmNotFoundException {
        log.info("Будет обновлен фильм {}", film.toString());
        findFilmInSetAndMayBeRemove(film);
        log.debug("Фильм помещен в таблицу");
        films.add(film);
        return film;
    }

    @Override
    public Film findFilmById(Long id) throws FilmNotFoundException {
        for (Film fl: films) {
            if (id.equals(fl.getId())) {
                return fl;
            }
        }
        throw new FilmNotFoundException("Фильм еще не существует");
    }

    @Override
    public String deleteFilm (Long id) throws FilmNotFoundException {
        log.info("Удаляем фильм с id {}", id);
        for (Film fl: films) {
            if (id.equals(fl.getId())) {
                if (fl.getLikes().isEmpty()) {
                    films.remove(fl);
                } else {
                    fl.getLikes().clear();
                    films.remove(fl);
                }
            }
            log.info("Фильм с id {} удален", id);
            return "Фильм удален";
        }
        throw new FilmNotFoundException("Фильм еще не существует");
    }

    private void findFilmInSet(Film film) throws ValidationException {
        for (Film fl: films) {
            if (fl.getId().equals(film.getId())) {
                log.debug("Фильм уже существует");
                throw new ValidationException("Фильм уже существует");
            }
        }
    }

    private void findFilmInSetAndMayBeRemove(Film film) throws FilmNotFoundException {
        for (Film fl: films) {
            if (fl.getId().equals(film.getId())) {
                films.remove(fl);
            } else {
                log.debug("Фильм еще не был добавлен");
                throw new FilmNotFoundException("Фильм еще не был добавлен");
            }
        }
    }
}
