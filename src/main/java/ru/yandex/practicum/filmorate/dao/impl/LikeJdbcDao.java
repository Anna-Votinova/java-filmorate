package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LikeJdbcDao implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToFilm(Long id, Long userId) {

        String sqlQuery = "insert into LIKES(FILMS_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void deleteLikeToFilm(Long id, Long userId) {
        String sqlQuery = "delete from LIKES where FILMS_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        String sqlQuery = "select * FROM FILMS f " +
                "left join (select FILMS_ID, count(*) likes_count " +
                "FROM LIKES group by FILMS_ID) l on f.ID = l.FILMS_ID" +
                " order by l.likes_count Desc LIMIT ?";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return new HashSet<>(films);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(LocalDate.ofEpochDay(resultSet.getLong("release_date")))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .rating(getMpaRatingById(resultSet.getInt("mpa_rating")))
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
}
