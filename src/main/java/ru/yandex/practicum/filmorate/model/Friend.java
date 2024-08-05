package ru.yandex.practicum.filmorate.model;
import lombok.Data;
/**
 * Представляет друга в приложении Filmorate.
 */
@Data
public class Friend {

    /**
     * Идентификатор Пользователя.
     * Источник отношений
     * sql column: user_id
     */
    private long id;

    /**
     * Идентификатор друга.
     * Цель отношений
     */
    private long friendId;
    /**
     * Идентификатор статуса дружбы.
     * Не подтверждена = 1.
     * Подтверждена = 2.
     */
    private int friendshipStatus;
}