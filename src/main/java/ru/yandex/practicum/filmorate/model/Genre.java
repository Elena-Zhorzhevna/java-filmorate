package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;

/**
 * Жанр фильма. Один фильм может иметь несколько жанров. Например:
 * Комедия.
 * Драма.
 * Мультфильм.
 * Триллер.
 * Документальный.
 * Боевик.
 */
@Data
@Getter
public class Genre {
    /**
     * Идентификатор фильма.
     */
    private long filmId;
    /**
     * Идентификатор жанра фильма.
     */
    private int id;
    /**
     * Название жанра.
     */
    private String name;

    @JsonCreator
    public Genre() {
    }
}