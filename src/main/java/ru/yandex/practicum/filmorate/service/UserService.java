package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    public UserService(@Qualifier("userJdbcDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public Set<User> findAll() {
        return userDao.findAll();
    }

    public User create(User user) throws ValidationException {
        setUserName(user);
        return userDao.create(user);
    }

    public User update(User user) throws UserNotFoundException {
        if (Objects.nonNull(user) && Objects.nonNull(findUserById(user.getId()))) {
            return userDao.update(user);
        }
        throw new UserNotFoundException("Юзер не может быть обновлен или найден");
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        userDao.deleteUser(id);
    }

    public User findUserById(Long id) throws UserNotFoundException {
        User finalUser;
        checkUserId(id);
        log.info("Попытка получить пользователя по id {}", id);
        finalUser = userDao.findUserById(id);
        return checkUser(finalUser);
    }

    private User checkUser(User user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("Юзер c таким id еще не занесен в таблицу");
        }
        return user;
    }

    private void checkUserId(Long id) throws UserNotFoundException {
        if (id < 1L) {
            throw new UserNotFoundException("Юзер не может быть обновлен или найден," +
                    "так как id не может быть меньше 1");
        }
    }

    private void setUserName(User user) {
        if (user.getName().isBlank()) {
            log.debug("Вместо имени установлен логин");
            user.setName(user.getLogin());
        }
    }
}