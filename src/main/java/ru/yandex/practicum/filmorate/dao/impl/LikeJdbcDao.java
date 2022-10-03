package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;

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
}
