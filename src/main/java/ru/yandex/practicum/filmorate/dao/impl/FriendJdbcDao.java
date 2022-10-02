package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class FriendJdbcDao implements FriendDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void delete(Long userId, Long friendId) {
        String sqlQuery = "delete from FRIENDS where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Friend save(User user, User friend) {
        log.info("Будет создан friend {} для user {}", friend.getId(),  user.getId());

        String sqlQuery = "insert into FRIENDS (user_id, friend_id, is_friend) " +
                "values ( ? , ? , ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setLong(1, user.getId());
            stmt.setLong(2, friend.getId());
            stmt.setBoolean(3, true);
            return stmt;
        }, keyHolder);

        return findFriendById(keyHolder.getKey().longValue());
    }

    @Override
    public Friend findFriendById(long id) {
        Friend friend;
        String sqlQuery = "select * from FRIENDS where id = ?";
        friend = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFriend, id);
        if(friend == null) {
            throw new UserNotFoundException("Друг не существует");
        }
        return friend;
    }

    @Override
    public List<Friend> findFriendsByUser(long userId) {
        String sql = "SELECT * FROM FRIENDS WHERE user_id = ?";

        List<Friend> result =  jdbcTemplate.query(
                sql,
                new Object[]{userId},
                (rs, rowNum) ->
                Friend.builder()
                                .id(rs.getLong("id"))
                                .userId(rs.getLong("user_id"))
                                .friendId(rs.getLong("friend_id"))
                                .isFriend(rs.getBoolean("is_friend"))
                                .build()
        );
        return result;
    }

    private Friend mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return Friend.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .isFriend(resultSet.getBoolean("is_friend"))
                .build();
    }
}
