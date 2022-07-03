package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Validated
@RestController
@Slf4j
public class UserController {

    private long idCounter = 1L;
    private Set<User> users = new HashSet<>();

    private void generateId(User user) {
        if (user.getId() == null) {
            log.debug("Сгенерирован новый id");
            user.setId(idCounter++);
        }
    }

    @GetMapping("/users")
    public Set<User> findAll() {
        log.debug("Текущее количество юзеров: {}", users.size());
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        checkUser(user);
        findUserInSet(user);
        checkUserName(user);
        log.info("Будет создан юзер {}", user.getName());
        generateId(user);
        log.debug("Пользователь добавлен в таблицу");
        users.add(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws ValidationException {
        log.info("Будет обновлен юзер {}", user.getName());
        findFilmInSetAndMayBeRemove(user);
        checkUserName(user);
        log.debug("Пользователь добавлен в таблицу");
        users.add(user);
        return user;
    }

    private void checkUser(User user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException();
        }
        log.info("Юзер не может быть добавлен, так как равен null");
    }

    private void findUserInSet(User user) throws ValidationException {
        for (User us: users) {
            if (us.getId().equals(user.getId())) {
                log.debug("Юзер уже существует");
                throw new ValidationException("Юзер уже существует");
            }
        }
    }

    private User checkUserName (User user) {
        if (user.getName().isBlank()) {
            log.debug("Вместо имени установлен логин");
            user.setName(user.getLogin());
        }
        return user;
    }

    private void findFilmInSetAndMayBeRemove(User user) throws ValidationException {
        for (User us: users) {
            if (us.getId().equals(user.getId())) {
                users.remove(us);
            } else {
                log.debug("Юзер еще не был добавлен");
                throw new ValidationException("Юзер еще не был добавлен");
            }
        }
    }
}
