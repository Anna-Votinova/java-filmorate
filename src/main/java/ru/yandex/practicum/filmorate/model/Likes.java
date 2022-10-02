package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Likes {
    private long id;
    private long filmId;
    private long userId;
}
