package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.time.LocalDate;
import java.util.List;

import static java.util.Calendar.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTests {
    private final FilmDao filmStorage;
    private final MpaRatingDao mpaRatingDao;
    private final GenresDao genresDao;
    public static Film film;

    @BeforeEach
    public void beforeEach() throws ValidationException {
    film = Film.builder()
            .name("Гарри Поттер")
            .description("Фильм о мальчике, который выжил")
            .releaseDate(LocalDate.of(2004, MAY, 31))
            .duration(142)
            .rating(mpaRatingDao.findRatingById(1))
            .genres(List.of(genresDao.findGenreById(1), genresDao.findGenreById(5)))
            .rate(5)
            .build();
    filmStorage.save(film);
    }

    @Test
    void shouldCreateFilm() {
        assertNotNull(film);
    }

    @Test
    void shouldGiveFilmByValidId() {
        Film newFilm = filmStorage.findFilmById(film.getId());
        assertEquals(film, newFilm);
    }

    @Test
    void shouldUpdateUser () throws UserNotFoundException, ValidationException {
        Film newFilm = Film.builder()
                .id(film.getId())
                .name("Гарри Поттер")
                .description("Темная сага о волшебстве")
                .releaseDate(LocalDate.of(2004, MAY, 31))
                .duration(200)
                .rating(mpaRatingDao.findRatingById(2))
                .genres(List.of(genresDao.findGenreById(1), genresDao.findGenreById(5)))
                .rate(5)
                .build();

        assertEquals(newFilm, filmStorage.update(newFilm));
    }

    @Test
    void shouldDeleteFilm () {
        filmStorage.deleteFilm(film.getId());
        assertEquals(null, filmStorage.findFilmById(film.getId()));
    }

    @Test
    void shouldFindAllFilms() throws ValidationException {
        Film newFilm = Film.builder()
                .name("Гарри Поттер и тайная комната")
                .description("Наконец все секреты будут раскрыты")
                .releaseDate(LocalDate.of(2005, MAY, 31))
                .duration(204)
                .rating(mpaRatingDao.findRatingById(2))
                .genres(List.of(genresDao.findGenreById(1), genresDao.findGenreById(5)))
                .rate(5)
                .build();

        Film newFilm2 = Film.builder()
                .name("Гарри Поттер и Узник Азкабана")
                .description("Три года в школе чародейства и волшебства")
                .releaseDate(LocalDate.of(2006, MAY, 31))
                .duration(201)
                .rating(mpaRatingDao.findRatingById(2))
                .genres(List.of(genresDao.findGenreById(1), genresDao.findGenreById(5)))
                .rate(5)
                .build();

        filmStorage.save(newFilm);
        filmStorage.save(newFilm2);

        List<Film> testFilmSet = filmStorage.findAll();
        assertEquals(3, testFilmSet.size());
    }
}
