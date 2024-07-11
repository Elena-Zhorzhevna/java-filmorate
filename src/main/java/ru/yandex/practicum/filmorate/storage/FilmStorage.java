package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAll(); //получение всех фильмов

    Film create(Film film); //добавление фильма

    Film update(Film newFilm); //обновление фильма

    Film findFilmById(Long filmId); //получение фильма по айди

    boolean isLiked(long filmId, long userId); //проверяет, есть ли у фильма лайк от данного пользователя

    void removeAllFilms(); //удаление всех фильмов
}