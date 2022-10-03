package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@AllArgsConstructor
public class MpaRatingController {
    private final MpaRatingService service;

    @GetMapping("/mpa")
    public List<MpaRating> findAll() {
        return service.findAll();
    }

    @GetMapping("/mpa/{id}")
    public MpaRating findRatingById(@PathVariable int id) throws MpaNotFoundException {
        return service.findRatingById(id);
    }
}
