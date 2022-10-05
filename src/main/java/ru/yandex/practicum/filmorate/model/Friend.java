package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@Builder
public class Friend {
    private Long id;
    private long userId;
    private long friendId;
    private boolean isFriend;
}
