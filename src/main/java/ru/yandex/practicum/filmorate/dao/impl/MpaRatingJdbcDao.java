package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;

import ru.yandex.practicum.filmorate.model.MpaRating;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;


@Component
@Data
public class MpaRatingJdbcDao implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRatingJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sqlQuery = "select * from MPA_RATING";

        List<MpaRating> mpaRatings = jdbcTemplate.query(sqlQuery, this::mapRowToMPARating);
        return mpaRatings;
    }

    @Override
    public MpaRating findRatingById(int id) {
        String sqlQuery = "select * from MPA_RATING where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPARating, id);
    }

    private MpaRating mapRowToMPARating(ResultSet resultSet, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
