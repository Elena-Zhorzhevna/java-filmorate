package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Представляет фильмы в приложении Filmorate.
 */
@Data
public class Film {
    /**
     * Идентификатор фильма.
     */
    private long id;
    /**
     * Назваине фильма.
     */
    private String name;
    /**
     * Описание фильма.
     */
    private String description;
    /**
     * Дата релиза фильма.
     */
    private LocalDate releaseDate;
    /**
     * Продолжительность фильма.
     */
    private int duration;
    /**
     * Свойство, содержащее список идентификаторов пользователей, поставивших лайк.
     */
    private Set<Long> likes = new HashSet<>();

    @JsonCreator
    public Film() {
    }

    /**
     * Конструкторы для тестов.
     */
    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    /**
     * Метод, добавляющий лайк указанному фильму.
     *
     * @param userId Идентификатор пользователя, лайк которого добавляется.
     */
    public void addLike(Long userId) {
        likes.add(id);
    }

    /**
     * Метод, удаляющий лайк у указанного фильма.
     *
     * @param id Идентификатор пользователя, лайк которого удаляется.
     */
    public void deleteLike(Long id) {
        likes.remove(id);
    }
}