package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long idCounter = 1L;
    private Set<User> users = new HashSet<>();


    private void generateId(User user) {
        if (user.getId() == null) {
            log.debug("Сгенерирован новый id");
            user.setId(idCounter++);
        }
    }

    @Override
    public Set<User> findAll() {
        log.debug("Текущее количество юзеров: {}", users.size());
        return users;
    }

    @Override
    public User create(User user) throws ValidationException {
        checkUser(user);
        findUserInSet(user);
        checkUserName(user);
        log.info("Будет создан юзер {}", user.getName());
        generateId(user);
        log.debug("Пользователь добавлен в таблицу");
        users.add(user);
        return user;
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        log.info("Будет обновлен юзер {}", user.getName());
        checkUserId(user);
        findFilmInSetAndMayBeRemove(user);
        checkFriendsSet(user);
        checkUserName(user);
        log.debug("Пользователь добавлен в таблицу");
        users.add(user);
        return user;
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        log.info("Ищем юзера с id {}", id);
        for (User us: users) {
            if (id.equals(us.getId()) && id > 0) {
                log.info("Возвращаем юзера с id {}", id);
                return us;
            }
        }
        throw new UserNotFoundException("Юзер еще не существует");
    }

    @Override
    public String deleteUser(Long id) throws UserNotFoundException {
        log.info("Удаляем юзера с id {}", id);
        for (User us: users) {
            if (id.equals(us.getId())) {
                if (us.getFriends().isEmpty()) {
                    users.remove(us);
                } else{
                    us.getFriends().clear();
                    users.remove(us);
                }
            }
            log.info("Юзер с id {} удален", id);
            return "Юзер удален";
        }
        throw new UserNotFoundException("Юзер еще не существует");
    }

    private void checkUser(User user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException();
        }
        log.info("Юзер не может быть добавлен, так как равен null");
    }

    private void checkUserId (User user) throws UserNotFoundException {
        if (user.getId() < 1L) {
            throw new UserNotFoundException("Юзер не может быть обновлен,так как id не может быть меньше 1");
        }
        log.info("Юзер не может быть обновлен,так как id не может быть меньше 1");
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

    private User checkFriendsSet (User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        return user;
    }

    private void findFilmInSetAndMayBeRemove(User user) throws UserNotFoundException {
        for (User us: users) {
            if (us.getId().equals(user.getId())) {
                users.remove(us);
            } else {
                log.debug("Юзер еще не был добавлен");
                throw new UserNotFoundException ("Юзер еще не был добавлен");
            }
        }
    }
}
