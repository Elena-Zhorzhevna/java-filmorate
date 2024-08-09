package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статус дружбы между двумя пользователями.
 * Неподтверждённая — когда один пользователь отправил запрос на добавление другого пользователя в друзья,
 * Подтверждённая — когда второй пользователь согласился на добавление.
 */
public class Friendship {
    /**
     * Идентификатор статуса дружбы между двумя пользователями.
     */
    private final int statusId;
    /**
     * Наименование статуса.
     */
    private final String statusName;

    @JsonCreator
    Friendship(int statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }
}