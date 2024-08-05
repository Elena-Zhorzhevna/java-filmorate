package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jdk.jshell.Snippet;
import lombok.*;

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
@Builder
@AllArgsConstructor
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

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

}