package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikeDao {

    void addLikeToFilm(Long id, Long userId);
    void deleteLikeToFilm(Long id, Long userId);
    Collection<Film> getPopularFilms (int count);

}
