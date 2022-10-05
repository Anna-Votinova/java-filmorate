package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {

    private final FriendDao friendDao;
    private final UserService userService;

    public void deleteFriend(Long id, Long friendId) {
        log.info("Пользователи c id {} и {} будут удалены друг у друга из друзей",
                id, friendId);
        friendDao.delete(id, friendId);
    }

    public Set<User> getFriendsOfUser(Long id) throws UserNotFoundException {
        log.info("Попытка получить id друзей пользователя по id {}", id);
        return friendDao.findFriendsByUser(id)
                .stream()
                .map(Friend::getFriendId)
                .map(userService::findUserById)
                .collect(Collectors.toSet());
    }

    public void addFriendToUser(Long id, Long friendId) throws UserNotFoundException {
        log.info("Добавляем пользователей c id {} и {} в друзья", id, friendId);
        if (id < 1L || friendId < 1L) {
            throw new UserNotFoundException("У кого-то из друзей id меньше 1");
        }

        User user = userService.findUserById(id);
        User friend = userService.findUserById(friendId);

        friendDao.save(user, friend);

        log.info("Пользователи c id {} и {} добавлены друг другу в друзья", user.getId(), friend.getId());
    }

    public Set<User> getCommonFriend(Long id, Long otherId) {
        log.info("Будет выведен общий список друзей пользователей c id {} и {}", id, otherId);

        Set<User> otherFriend = getFriendsOfUser(otherId);
        Set<User> userFriend = getFriendsOfUser(id);

        Set<User> result = new HashSet<>();
        for (User user : otherFriend) {
            for (User user1 : userFriend) {
                if (user.getId().equals(user1.getId())) {
                    result.add(user);
                }
            }
        }

        log.info(String.format("Выведен общий список друзей пользователей c id %s и %s", id, otherId));

        return result;
    }
}
