package ru.yandex.practicum.filmorate.storage.dto.modelDto;

import lombok.Data;

/**
 * Класс представляет объект, который будет возвращать сервис при запросе данных о рейтинге фильма.
 */
@Data
public class RatingMpaDto {
    private int id;
    private String name;
}
