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
    public void shouldTrowValidationExpressionWhenEmailIsEmpty () {
        user.setEmail("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введен некорректный email", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenEmailHasOnlyOneGap () {
        user.setEmail(" ");
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введен некорректный email", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenEmailHasNoChar () {
        user.setEmail("anna1336on.ru");
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введен некорректный email", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenLoginIsEmpty () {
        user.setLogin("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введен некорректный логин", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenLoginHasBlanks () {
        user.setLogin("Anna 13");
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введен некорректный логин", exception.getMessage());
    }

    @Test
    public void shouldTrowValidationExpressionWhenBirthdayIsInFuture () {
        user.setBirthday(LocalDate.of(2022, DECEMBER,13));
        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Введена некорректная дата рождения", exception.getMessage());
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
    public void createNewUser_createdUserShouldBeNotNull() {
        user = User.builder().build();
        Exception exception = assertThrows(NullPointerException.class, () -> userController.create(user));
    }

}
