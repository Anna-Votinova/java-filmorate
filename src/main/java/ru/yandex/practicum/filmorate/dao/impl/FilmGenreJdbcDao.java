package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Component
public class FilmGenreJdbcDao implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public FilmGenre save(FilmGenre filmGenre) {
        String sqlQuery = "insert into FILMS_GENRES (genre_id, film_id) " +
                "values ( ? , ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setInt(1, filmGenre.getGenreId());
            stmt.setLong(2, filmGenre.getFilmId());
            return stmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue());
    }

    public FilmGenre findById(long id) {
        String sqlQuery = "select * from FILMS_GENRES where id = ?";
        FilmGenre filmGenre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

        return filmGenre;
    }

    @Override
    public List<FilmGenre> findAll(Film film) {
        String sql = "SELECT * FROM FILMS_GENRES WHERE film_id = ?";

        List<FilmGenre> result =  jdbcTemplate.query(
                sql,
                new Object[]{film.getId()},
                (rs, rowNum) ->
                        FilmGenre.builder()
                                .id(rs.getLong("id"))
                                .filmId(rs.getLong("film_id"))
                                .genreId(rs.getInt("genre_id"))
                                .build()
        );

        return result;
    }

    private FilmGenre mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .id(resultSet.getLong("id"))
                .filmId(resultSet.getLong("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .build();
    }

    @Override
    public void delete(long filmId, long genreId) {
        String sqlQuery = "delete from FILMS_GENRES where genre_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, genreId, filmId);
    }
}
