package ru.yandex.practicum.filmorate.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
public class Genre {
    private int id;
    @NotBlank
    private String name;
}
