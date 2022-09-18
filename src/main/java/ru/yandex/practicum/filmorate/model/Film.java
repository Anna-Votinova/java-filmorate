package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.utilities.AfterOrEqualData;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание фильма не должно быть пустым")
    @Size(min = 1, max = 200, message = "Описание фильма не должно превышать 200 знаков")
    private String description;
    @NotNull(message = "Дата релиза не должна равняться null")
    @AfterOrEqualData(movieDay = "1895-12-28", message = "Дата выхода не может быть раньше Дня основания кино")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной или равна нулю")
    private long duration;
    private long rate;
    private Set<Long> likes;
    private List<Genre> genres;
    private MPARating rating;
}
