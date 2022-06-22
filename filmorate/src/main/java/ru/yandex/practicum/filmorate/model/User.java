package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String name;
    private String login;
    @NotNull
    private LocalDate birthday;
}
