package ru.yandex.practicum.filmorate.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Validated
@RestController
public class FilmController {

private final FilmStorage storage;
private final FilmService service;

    public FilmController(FilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping("/films")
    public Set<Film> findAll() {
        return storage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Long id) throws FilmNotFoundException {
        return storage.findFilmById(id);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return storage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update (@Valid @RequestBody Film film) throws FilmNotFoundException {
        return storage.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikesToSetOfFilm(@PathVariable Long id, @PathVariable Long userId) throws UserNotFoundException, FilmNotFoundException {
        service.addLikesToSetOfFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public String deleteLikesToSetOfFilm (@PathVariable Long id, @PathVariable Long userId) throws UserNotFoundException, FilmNotFoundException {
        return service.deleteLikesToSetOfFilm(id,userId);
    }

    @DeleteMapping("/films/{id}")
    public String deleteFilm(@PathVariable Long id) throws FilmNotFoundException {
        return storage.deleteFilm(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms (@RequestParam(value = "count", defaultValue = "10", required = false) final int count) {
        return service.getPopularFilms(count);
    }
}
