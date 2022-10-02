package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.GenreRequestDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.GenresService;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FilmMapper {

    private final MpaRatingService mpaRatingService;
    private final GenresService genresService;

    public Film toFilm(FilmRequestDto filmDto) {

        MpaRating mpaRating = mpaRatingService.findRatingById(filmDto.getMpa().getId());

        List<Genre> genres = null;
        if (Objects.nonNull(filmDto.getGenres())) {
             genres = filmDto.getGenres()
                     .stream()
                     .map(GenreRequestDto::getId)
                     .distinct()
                     .map(genresService::findGenreById)
                     .collect(Collectors.toList());
        }


        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .rate(filmDto.getRate())
                .mpa(mpaRating)
                .genres(genres)
                .build();
    }
}
