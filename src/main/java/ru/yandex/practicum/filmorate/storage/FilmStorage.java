package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс, в котором определены методы добавления, удаления и модификации объектов Film.
 */
public interface FilmStorage {
    /**
     * Получение всех фильмов.
     */
    List<Film> findAll();

    /**
     * Добавление фильма.
     */
    Film create(Film film);

    /**
     * Обновление фильма.
     */
    Film update(Film newFilm);

    /**
     * Получение фильма по идентификатору.
     */
    Film findFilmById(Long filmId);

    /**
     * Метод, проверяющий наличие лайка у данного пользователя.
     */
    //boolean isLiked(long filmId, long userId);

    /**
     * Удаление всех фильмов.
     */
    void removeAllFilms();
}