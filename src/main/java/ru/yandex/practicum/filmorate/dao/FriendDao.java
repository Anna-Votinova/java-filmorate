package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    Friend save(User user, User friend);

    Friend findFriendById(long id);

    List<Friend> findFriendsByUser(long userId);

    void delete(Long userId, Long friendId);
}
