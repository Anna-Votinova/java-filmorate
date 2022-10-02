package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.UserJdbcDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoTests {
    private final UserJdbcDao userStorage;
    private static User user;

    @BeforeEach
    public void beforeEach() throws ValidationException {
        user = User.builder()
                .email("anna13@36on.ru")
                .login("Anna13")
                .name("Anna")
                .birthday(LocalDate.of(1990, DECEMBER,13))
                .build();
        user = userStorage.create(user);
    }

    @Test
    void shouldCreateUser() {
        assertNotNull(user);
    }

    @Test
    void shouldGiveUserByValidId() {
        User newUser = userStorage.findUserById(user.getId());
        assertEquals(user, newUser);
    }

    @Test
    void shouldUpdateUser () throws UserNotFoundException {
        User newUser = User.builder()
                .id(user.getId())
                .email("anna13@36on.ru")
                .login("AnnaVotinova")
                .name("Anna")
                .birthday(LocalDate.of(1990, DECEMBER,13))
                .build();

        assertEquals(newUser, userStorage.update(newUser));
    }

    @Test
    void shouldDeleteUser ()  {
        userStorage.deleteUser(user.getId());
        assertEquals(null, userStorage.findUserById(user.getId()));
    }

    @Test
    void shouldFindAllUsers() throws ValidationException {
        User newUser = User.builder()
                .email("anna@36on.ru")
                .login("Votinova")
                .name("Votinova")
                .birthday(LocalDate.of(1990, DECEMBER,13))
                .build();

        User newUser2 = User.builder()
                .email("13@36on.ru")
                .login("13Votinova")
                .name("13")
                .birthday(LocalDate.of(1990, DECEMBER,13))
                .build();
        userStorage.create(newUser);
        userStorage.create(newUser2);

        Set<User> testUserSet = userStorage.findAll();
        assertEquals(3, testUserSet.size());
    }
}
