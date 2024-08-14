package ru.yandex.practicum.filmorate.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;

/**
 * Класс для проверки наличия у объекта Film существования жанров и рейтинга МПА.
 */
@Component
@RequiredArgsConstructor
public class FilmValidatorNew {
    private final GenreDbStorage genreDbStorage;
    private final RatingMpaDbStorage ratingMpaDbStorage;

    public void validate(Film film) {
        /* Валидация, что все жанры существуют */
        film.getGenres().forEach(it -> {
            if (!genreDbStorage.contains((long) it.getId()))
                throw new ValidationException("Не Существует жанра с id=\"" + it.getId() + "\"");
        });

        /* Валидация, что рейтинг МПА существует */
        if (!ratingMpaDbStorage.contains((long) film.getMpa().getId())) {
            throw new ValidationException("Не существует MPA рейтинга с id=\"" + film.getMpa().getId() + "\"");
        }
    }
}