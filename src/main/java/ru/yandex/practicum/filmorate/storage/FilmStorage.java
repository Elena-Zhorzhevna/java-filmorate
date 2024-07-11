package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAll();
    Film create(Film film);
    Film update(Film newFilm);
    Film findFilmById(Long filmId);
    boolean isLiked(long filmId, long userId);
}