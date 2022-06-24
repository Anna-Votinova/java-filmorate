package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.HashSet;
import java.util.Set;


@RestController
@Slf4j
public class UserController {

    private long idCounter = 1L;
    private LocalDateTime currentTimestamp = LocalDateTime.now();
    private Set<User> users = new HashSet<>();

    protected void setIdCounter(Long idCounter) {
        this.idCounter = idCounter;
    }

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
        log.info("Будет создан юзер {}", user.getName());
            for (User us: users) {
                if (us.getId().equals(user.getId())) {
                    log.debug("Юзер уже существует");
                    throw new ValidationException("Юзер уже существует");
                }
            }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Введен некорректный email");
            throw new ValidationException("Введен некорректный email");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                log.debug("Введен некорректный логин");
                throw new ValidationException("Введен некорректный логин");
            } else if (user.getBirthday().isAfter(ChronoLocalDate.from(currentTimestamp))) {
                log.debug("Введена некорректная дата рождения");
                throw new ValidationException("Введена некорректная дата рождения");
            } else if (user.getName().isBlank()) {
                log.debug("Вместо имени установлен логин");
                user.setName(user.getLogin());
            }
            generateId(user);
            log.debug("Пользователь добавлен в таблицу");
            users.add(user);

        return user;
    }

    //настроить ручную проверку аннотаций в классе UserController - как?
    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws ValidationException {
        log.info("Будет обновлен юзер {}", user.getName());
            for (User us: users) {
                if (us.getId().equals(user.getId())) {
                    users.remove(us);
                } else {
                    log.debug("Юзер еще не был добавлен");
                    throw new ValidationException("Юзер еще не был добавлен");
                }
            }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Введен некорректный email");
            throw new ValidationException("Введен некорректный email");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                log.debug("Введен некорректный логин");
                throw new ValidationException("Введен некорректный логин");
            } else if (user.getBirthday().isAfter(ChronoLocalDate.from(currentTimestamp))) {
                log.debug("Введена некорректная дата рождения");
                throw new ValidationException("Введена некорректная дата рождения");
            } else if (user.getName().isBlank()) {
                log.debug("Вместо имени установлен логин");
                user.setName(user.getLogin());
            }
            log.debug("Пользователь добавлен в таблицу");
            users.add(user);

        return user;
    }
}
