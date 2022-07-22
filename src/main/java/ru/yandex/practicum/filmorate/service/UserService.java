package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User findUserById(Long id) throws UserNotFoundException {
        log.info("Попытка получить пользователя по id {}", id);
        return storage.findUserById(id);
    }

    public void addToSetOfFriends (Long id, Long friendId) throws UserNotFoundException {
        log.info("Добавляем пользователей c id {} и {} в друзья", id, friendId);
        if (id < 1L || friendId < 1L) {
            throw new UserNotFoundException("У кого-то из друзей id меньше 1");
        }
        User user = findUserById(id);
        Set<Long> userFriends = checkNullSet(user.getFriends());
        userFriends.add(friendId);
        user.setFriends(userFriends);

        User friend = findUserById(friendId);
        Set<Long> friendFriends = checkNullSet(friend.getFriends());
        friendFriends.add(id);
        friend.setFriends(friendFriends);

        log.info("Пользователи c id {} и {} добавлены друг другу в друзья", id, friendId);
    }

    public Set<Long> getFriendsOfUser(Long id) throws UserNotFoundException {
        log.info("Попытка получить id друзей пользователя по id {}", id);
        return checkNullSet(findUserById(id).getFriends());
    }

    public String deleteUserByIdFromSetOfFriends (Long id, Long friendId) throws UserNotFoundException {
        log.info("Пользователи c id {} и {} будут удалены друг у друга из друзей",
                id, friendId);

        User user = findUserById(id);
        Set<Long> userFriends = checkNullSet(user.getFriends());
        userFriends.remove(friendId);
        user.setFriends(userFriends);

        User friend = findUserById(friendId);
        Set<Long> friendFriends = checkNullSet(friend.getFriends());
        friendFriends.remove(id);
        friend.setFriends(friendFriends);

        //findUserById(id).getFriends().remove(findUserById(friendId).getId());
        //findUserById(friendId).getFriends().remove(findUserById(id).getId());
        log.info("Пользователи c id {} и {} удалены друг у друга из друзей", id, friendId);
        return String.format("%d и %d больше не друзья", id, friendId);
    }

    public Set<Long> getListOfCommonFriends(Long id, Long friendId) throws UserNotFoundException {
        log.info("Будет выведен общий список друзей пользователей c id {} и {}",
                id, friendId);

        Set<Long> common = new HashSet<>();
        Set<Long> idFriends = checkNullSet(findUserById(id).getFriends());
        Set<Long> idFriendsId = checkNullSet(findUserById(friendId).getFriends());
        log.info(String.format("Выведен общий список друзей пользователей c id {} и {}",
                id, friendId));

        common.addAll(idFriendsId);
        common.addAll(idFriends);
        common.remove(id);
        common.remove(friendId);

        return common;
    }

    private Set<Long> checkNullSet(Set<Long> set) {
        if (Objects.isNull(set)) {
            return new HashSet<>();
        }
        return set;
    }
}
