package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@Data
public class GenresJdbcDao implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "select * from GENRES";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        return genres;
    }

    @Override
    public Genre findGenreById(long id) {
        String sqlQuery = "select * from GENRES where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
