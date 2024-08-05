package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

/**
 * Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА).
 * Эта оценка определяет возрастное ограничение для фильма. Значения могут быть следующими:
 * G — у фильма нет возрастных ограничений,
 * PG — детям рекомендуется смотреть фильм с родителями,
 * PG-13 — детям до 13 лет просмотр не желателен,
 * R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
 * NC-17 — лицам до 18 лет просмотр запрещён.
 */
@Data
public class RatingMpa {
    /**
     * Идентификатор рейтинга.
     */
    private int id;
    /**
     * Название рейтинга.
     */
    private String name;
    /**
     * Описание рейтинга.
     */
    private String description;
    @JsonCreator
    public RatingMpa() {
    }

    public RatingMpa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}