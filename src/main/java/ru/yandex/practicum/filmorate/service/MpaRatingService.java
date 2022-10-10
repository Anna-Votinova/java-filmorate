package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingJdbcDao;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MpaRatingService {

    private MpaRatingJdbcDao storage;

    public List<MpaRating> findAll() {
        return storage.findAll();
    }
    public MpaRating findRatingById(int id) {
        if (id < 1) {
            throw new MpaNotFoundException("Такого рейтинга не существует");
        }
        return storage.findRatingById(id);
    }
}


