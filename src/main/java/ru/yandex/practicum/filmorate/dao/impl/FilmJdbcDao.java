package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmPopularCount;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@AllArgsConstructor
public class FilmJdbcDao implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findPopularFilm(Integer limit) {

        String sqlQuery = "select f.id, count(l.USER_ID) as sum_c " +
                "from films f left join likes l on f.id = l.films_id " +
                "group by f.id " +
                "order by sum_c desc";

        List<FilmPopularCount> result =  jdbcTemplate.query(
                sqlQuery,
                (rs, rowNum) -> FilmPopularCount.builder()
                        .filmId(rs.getLong("id"))
                        .count(rs.getLong("sum_c"))
                        .build()
        );

        List<Film> finalFilms = result.stream()
                .map(FilmPopularCount::getFilmId)
                .map(this::findFilmById)
                .limit(limit)
                .collect(Collectors.toList());

        return finalFilms;
    }

    @Override
    public Film save(Film film) {

        String sqlQuery = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_RATING) " +
                    "values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getRate());
                stmt.setInt(6, film.getMpa().getId());
                return stmt;
            }, keyHolder);

        return findFilmById(keyHolder.getKey().longValue());

    }

    @Override
    public Film update(Film film) {

        String sqlQuery = "update FILMS set " +
                    "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                    "DURATION= ?, RATE = ?, MPA_RATING = ? " +
                    "where id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Long id) {
        String sqlQuery = "select * from FILMS where id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate((resultSet.getDate("release_date").toLocalDate()))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(getMpaRatingById(resultSet.getInt("mpa_rating")))
                .build();
    }

    private MpaRating getMpaRatingById(Integer id) {
        String sqlQuery = "select * from MPA_RATING where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPARating, id);
    }

    private MpaRating mapRowToMPARating(ResultSet resultSet, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public void deleteFilm(Long id) throws FilmNotFoundException {

            String sqlQuery = "delete from FILMS where id = ?";
            jdbcTemplate.update(sqlQuery, id);

    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from FILMS";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return films;
    }
}
