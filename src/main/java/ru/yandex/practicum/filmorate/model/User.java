package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NotNull(message = "Введен некорректный email")
    @Email(message = "Введен некорректный email")
    private String email;
    private String name;
    @NotEmpty(message = "Введен некорректный логин")
    @Pattern(regexp = "^\\S*$", message = "Введен некорректный логин")
    private String login;
    @NotNull(message = "Введена некорректная дата рождения")
    @Past(message = "Введена некорректная дата рождения")
    private LocalDate birthday;
}
