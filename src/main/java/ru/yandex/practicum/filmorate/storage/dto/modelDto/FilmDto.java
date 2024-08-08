package ru.yandex.practicum.filmorate.storage.dto.modelDto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс представляет объект, который будет возвращать сервис при запросе данных о фильме.
 */
@Data
public class FilmDto {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes = new HashSet<>();
    private List<GenreDto> genres = new ArrayList<>();
    private RatingMpaDto mpa;
}