package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Long id) throws FilmNotFoundException {
        return filmService.findFilmById(id);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody FilmRequestDto filmDto) throws ValidationException {
        Film film = filmMapper.toFilm(filmDto);
        Film created = filmService.save(film);
        return created;
    }

    @PutMapping(value = "/films")
    public Film update (@Valid @RequestBody FilmRequestDto filmDto) throws FilmNotFoundException {
        Film film = filmMapper.toFilm(filmDto);
        Film updated = filmService.update(film);
        return updated;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeToToFilm(@PathVariable Long id, @PathVariable Long userId) throws UserNotFoundException, FilmNotFoundException {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public String deleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) throws UserNotFoundException, FilmNotFoundException {
        return filmService.deleteLikeToFilm(id,userId);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable Long id) throws FilmNotFoundException {
        filmService.deleteFilm(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms (@RequestParam(value = "count", defaultValue = "10", required = false) final int count) {
        return filmService.getPopularFilms(count);
    }
}
