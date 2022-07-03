package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.*;


public class UserControllerTest  {
    private static UserController userController;
    private static User user;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user = User.builder()
                .email("anna13@36on.ru")
                .login("Rainbow")
                .name("Anna")
                .birthday(LocalDate.of(1990, DECEMBER,13))
                .build();
    }

    @Test
    public void shouldUseLoginWhenNameIsEmpty () throws ValidationException {
        user.setName("");
        String expectedName = user.getLogin();
        String resultName = userController.create(user).getName();
        assertEquals(expectedName, resultName);
    }

    @Test
    public void shouldTrowValidationExpressionWhenTheSameUserAddsToMap () throws ValidationException {
        user = userController.create(user);
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Юзер уже существует", exception.getMessage());

    }

    @Test
    public void createNewUser_createdUserShouldBeNotNull() throws NullPointerException {
        user = User.builder().build();
        Exception exception = assertThrows(NullPointerException.class, () -> userController.create(user));
    }

}
