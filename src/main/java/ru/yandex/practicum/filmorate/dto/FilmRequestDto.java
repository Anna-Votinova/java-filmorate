package ru.yandex.practicum.filmorate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.utilities.AfterOrEqualData;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FilmRequestDto {

    private long id;

    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    @NotNull(message = "Дата релиза не должна равняться null")
    @AfterOrEqualData(movieDay = "1895-12-28", message = "Дата выхода не может быть раньше Дня основания кино")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotBlank(message = "Описание фильма не должно быть пустым")
    @Size(min = 1, max = 200, message = "Описание фильма не должно превышать 200 знаков")
    private String description;

    @Positive(message = "Продолжительность фильма не может быть отрицательной или равна нулю")
    private int duration;

    private int rate;

    @NotNull
    private MpaRequestDto mpa;

    private List<GenreRequestDto> genres;
}
