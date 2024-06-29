package ru.yandex.practicum.filmorate.validator;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public final class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    public static boolean isValidFilm(@RequestBody Film film) {
        // проверяем выполнение необходимых условий

        if (!isValidName(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (!isValidDescription(film.getDescription())) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }

        if (!isValidReleaseDate(film.getReleaseDate())) {
            throw new ValidationException("Дата релиза не может быть раньше создания кино.");
        }

        if (!isValidDuration(film.getDuration())) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
        return true;
    }

    //название не может быть пустым
    private static boolean isValidName(String name) {
        return !(name == null || name.isBlank());
    }

    //максимальная длина описания — 200 символов
    private static boolean isValidDescription(String description) {
        return description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    //дата релиза не может быть раньше создания кино
    private static boolean isValidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isAfter(MIN_DATE);
    }

    //продолжительность фильма должна быть положительным числом
    private static boolean isValidDuration(int duration) {
        return duration > 0;
    }
}