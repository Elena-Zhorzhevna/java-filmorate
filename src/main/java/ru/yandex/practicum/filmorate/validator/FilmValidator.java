package ru.yandex.practicum.filmorate.validator;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

/**
 * Класс для валидации объекта Film.
 * Проверка перед сохранением в базу
 */
public final class FilmValidator {
    /**
     * Максимально допустимое количество символов в строке описания фильма.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    /**
     * Дата создания кино.
     */
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    /**
     * Проверяем выполнение необходимых условий.
     *
     * @param film Фильм для проверки валидации.
     * @return Является ли объект удовлетворяющим требованиям.
     */
    public static boolean isValidFilm(@RequestBody Film film) {
        if (!isValidName(film.getName()))
            throw new ValidationException("Название фильма не может быть пустым.");
        if (!isValidDescription(film.getDescription()))
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        if (!isValidReleaseDate(film.getReleaseDate()))
            throw new ValidationException("Дата релиза не может быть раньше создания кино.");
        if (!isValidDuration(film.getDuration()))
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        return true;
    }

    /**
     * Название не может быть пустым.
     *
     * @param name Название фильма для проверки валидации.
     * @return Является ли название удовлетворяющим условиям.
     */
    private static boolean isValidName(String name) {
        return !(name == null || name.isBlank());
    }

    /**
     * Максимальная длина описания — 200 символов.
     *
     * @param description Строка описания фильма для проверки валидации.
     * @return Является ли описание удовлетворяющим условиям.
     */
    private static boolean isValidDescription(String description) {
        return description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    /**
     * Дата релиза фильма не может быть раньше даты создания кино.
     *
     * @param releaseDate Проверяемая дата создания фильма.
     * @return Является ли дата удовлетворяющей условиям.
     */
    private static boolean isValidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isAfter(MIN_DATE);
    }

    /**
     * Продолжительность фильма должна быть положительным числом.
     *
     * @param duration Проверяемая продолжитпльность фильма.
     * @return Является ли продолжительность удовлетворяющей условиям.
     */
    private static boolean isValidDuration(int duration) {
        return duration > 0;
    }

}