package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotFoundException extends RuntimeException{

    public GenreNotFoundException(String message) {
        super(message);
    }
}
