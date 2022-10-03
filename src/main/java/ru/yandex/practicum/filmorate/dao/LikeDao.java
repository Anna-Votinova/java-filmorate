package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {

    void addLikeToFilm(Long id, Long userId);
    void deleteLikeToFilm(Long id, Long userId);

}
