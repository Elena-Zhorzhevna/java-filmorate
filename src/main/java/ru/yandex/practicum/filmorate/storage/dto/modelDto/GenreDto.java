package ru.yandex.practicum.filmorate.storage.dto.modelDto;

import lombok.Data;

/**
 * Класс представляет объект, который будет возвращать сервис при запросе данных о жанре.
 */
@Data
public class GenreDto {
    private int id;
    private String name;
}
