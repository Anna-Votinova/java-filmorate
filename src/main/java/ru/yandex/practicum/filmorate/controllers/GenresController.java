package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenresService service;

    @GetMapping("/genres")
    public List<Genre> findAll() {
        return service.findAll();
    }

    @GetMapping("/genres/{id}")
    public Genre getUserById(@PathVariable int id) throws GenreNotFoundException {
        return service.findGenreById(id);
    }
}
