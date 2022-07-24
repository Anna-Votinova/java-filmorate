package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    Set<User> findAll();
    User create(User user) throws ValidationException;
    User update(User user) throws UserNotFoundException;
    User findUserById(Long id) throws UserNotFoundException;
    String deleteUser(Long id) throws UserNotFoundException;
}
