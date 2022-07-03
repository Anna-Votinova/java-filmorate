package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private static FilmController filmController = new FilmController();
    private static Film film;

    @Test
    public void shouldTrowValidationExpressionWhenTheSameFilmAddsToMap () throws ValidationException {
        film = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(142)
                .build();
        film = filmController.create(film);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Фильм уже существует", exception.getMessage());

    }
}
