package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserDao {
    Set<User> findAll();
    User create(User user) throws ValidationException;
    User update(User user) throws UserNotFoundException;
    User findUserById(Long id) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
}
