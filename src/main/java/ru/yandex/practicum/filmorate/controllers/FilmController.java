package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


import static java.time.Month.DECEMBER;

@RestController
@Slf4j
public class FilmController {

    private long idCounter = 1L;
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, DECEMBER, 28);
    private Set<Film> films = new HashSet<>();

    protected void setIdCounter(Long idCounter) {
        this.idCounter = idCounter;
    }

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
             for (Film fl: films) {
                if (fl.getId().equals(film.getId())) {
                    log.debug("Фильм уже существует");
                    throw new ValidationException("Фильм уже существует");
                }
            }
            if (film.getName().isBlank()) {
                log.debug("Название фильма не должно быть пустым");
                throw new ValidationException("Название фильма не должно быть пустым");
            } else if (film.getDescription().length() > 200) {
                log.debug("Описание фильма не должно превышать 200 знаков");
                throw new ValidationException("Описание фильма не должно превышать 200 знаков");
            } else if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
                log.debug("Дата выхода не может быть раньше Дня основания кино");
                throw new ValidationException("Дата выхода не может быть раньше Дня основания кино");
            } else if (film.getDuration() <= 0) {
                log.debug("Продолжительность фильма не может быть отрицательной или равна нулю");
                throw new ValidationException("Продолжительность фильма не может быть отрицательной или равна нулю");
            }
            generateId(film);
            log.debug("Фильм помещен в таблицу");
            films.add(film);

        return film;
    }


    @PutMapping(value = "/films")
    public Film update (@Valid @RequestBody Film film) throws ValidationException {
        log.info("Будет обновлен фильм {}", film.toString());
            for (Film fl: films) {
                if (fl.getId().equals(film.getId())) {
                    films.remove(fl);
                } else {
                    log.debug("Фильм еще не был добавлен");
                    throw new ValidationException("Фильм еще не был добавлен");
                }
            } if (film.getName().isBlank()) {
                log.debug("Название фильма не должно быть пустым");
                throw new ValidationException("Название фильма не должно быть пустым");
            } else if (film.getDescription().length() > 200) {
                log.debug("Описание фильма не должно превышать 200 знаков");
                throw new ValidationException("Описание фильма не должно превышать 200 знаков");
            } else if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
                log.debug("Дата выхода не может быть раньше Дня основания кино");
                throw new ValidationException("Дата выхода не может быть раньше Дня основания кино");
            } else if (film.getDuration() <= 0) {
                log.debug("Продолжительность фильма не может быть отрицательной или равна нулю");
                throw new ValidationException("Продолжительность фильма не может быть отрицательной или равна нулю");
            }
            log.debug("Фильм помещен в таблицу");
            films.add(film);

        return film;
    }
}
