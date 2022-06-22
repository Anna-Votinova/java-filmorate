package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private static FilmController filmController;
    private static Film film;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldTrowValidationExpressionWhenNameIsEmpty () {
        film = Film.builder()
                .name("")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(142)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Название фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenNameHasOnlyOneGap () {
        film = Film.builder()
                .name(" ")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(142)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Название фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenDescriptionLengthMoreThan200Chars () {
        film = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Гарри Поттер и Узник Азкабана» — это третий фильм из серии о Гарри Поттере. " +
                        "Он вышел в прокат 31 мая 2004 года в Великобритании и Ирландии, " +
                        "а 4 июня — в России, Мексике, США и Канаде. Основных персонажей сыграли те же актёры, " +
                        "что и в первых двух фильмах, за исключением Альбуса Дамблдора.")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(142)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Описание фильма не должно превышать 200 знаков", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenFilmReleaseDateIsEarlierThanMovieFoundationDay () {
        film = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(1893, MAY, 31))
                .duration(142)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата выхода не может быть раньше Дня основания кино", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenDurationIsNegative () {
        film = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(-142)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма не может быть отрицательной или равна нулю", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenDurationIsZero () {
        film = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(0)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма не может быть отрицательной или равна нулю", exception.getMessage());
    }

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

    @Test
    public void createNewFilm_createdFilmShouldBeNotNull() {
        film = Film.builder().build();
        Exception exception = assertThrows(NullPointerException.class, () -> filmController.create(film));
    }

}
