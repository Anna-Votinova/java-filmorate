package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenresJdbcDao;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenresService {

    private GenresJdbcDao storage;

    @Autowired
    public GenresService(GenresJdbcDao storage) {
        this.storage = storage;
    }

   public List<Genre> findAll (){
        return storage.findAll();
   }
   public Genre findGenreById(long id) {
       if (id < 1) {
           throw new GenreNotFoundException("Такого рейтинга не существует");
       }
       return storage.findGenreById(id);
   }
}
