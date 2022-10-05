package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class UserJdbcDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws ValidationException {
        log.info("Будет создан юзер {}", user.getName());

        String sqlQuery = "insert into USERS (EMAIL, NAME, LOGIN, BIRTHDAY) " +
                "values ( ? , ? , ? , ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        return findUserById(keyHolder.getKey().longValue());
    }

    @Override
    public User update(User user) throws UserNotFoundException {

        String sqlQuery = "update USERS set " +
                "EMAIL = ? , NAME = ? , LOGIN = ? , BIRTHDAY = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());

        return findUserById(user.getId());
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        User user;
        String sqlQuery = "select * from USERS where id = ?";
        user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        if(user == null) {
            throw new UserNotFoundException("Юзер не существует");
        }
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday((resultSet.getDate("birthday").toLocalDate()))
                .build();
    }

    @Override
    public void deleteUser(Long id) {
        String sqlQuery = "delete from USERS where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Set<User> findAll() {
        String sqlQuery = "select * from USERS";

        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        return new HashSet<>(users);
    }

}
