package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Validated
@RestController
@Slf4j
public class FilmController {

    private long idCounter = 1L;
    private Set<Film> films = new HashSet<>();

    private void generateId(Film film) {
        if (film.getId() == null) {
            log.debug("Сгенерирован новый id");
            film.setId(idCounter++);
        }
    }

    @GetMapping("/films")
    public Set<Film>findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Будет создан фильм {}", film.toString());
        findFilmInSet(film);
        generateId(film);
        log.debug("Фильм помещен в таблицу");
        films.add(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update (@Valid @RequestBody Film film) throws ValidationException {
        log.info("Будет обновлен фильм {}", film.toString());
        findFilmInSetAndMayBeRemove(film);
        log.debug("Фильм помещен в таблицу");
        films.add(film);
        return film;
    }

    private void findFilmInSet(Film film) throws ValidationException {
        for (Film fl: films) {
            if (fl.getId().equals(film.getId())) {
                log.debug("Фильм уже существует");
                throw new ValidationException("Фильм уже существует");//возможно просто заменить на Sop и return
            }
        }
    }

    private void findFilmInSetAndMayBeRemove(Film film) throws ValidationException {
        for (Film fl: films) {
            if (fl.getId().equals(film.getId())) {
                films.remove(fl);
            } else {
                log.debug("Фильм еще не был добавлен");
                throw new ValidationException("Фильм еще не был добавлен");
            }
        }
    }
}
