package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

import java.util.Collection;


/**
 * Класс контроллера для управления фильмами в приложении Filmorate.
 */
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    /**
     * Обрабатывает GET-запрос на получение всех фильмов.
     *
     * @return Коллекция всех фильмов.
     */
    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.getAllFilms();
    }

    /**
     * Обрабатывает GET - запрос на получение фильма по идентификатору.
     *
     * @param filmId Идентификатор фильма.
     * @return Фильм с указанным идентификатором.
     */
    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable("filmId") long filmId) {
        return filmService.getFilmById(filmId);
    }

    /**
     * Обрабатывает POST-запросы на добавление нового фильма.
     *
     * @param film Фильм, который нужно добавить.
     * @return Добавленный фильм.
     */
    @PostMapping
    public FilmDto create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    /**
     * Обрабатывает PUT-запрос на обновление существующего фильма.
     *
     * @param newFilm Фильм с обновленной информацией.
     * @return Обновленный фильм.
     */
    @PutMapping
    public FilmDto update(@RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    /**
     * Обрабатывает PUT-запрос на добавление лайка от пользователя фильму с указанным идентификатором.
     *
     * @param filmId Идентификатор фильма, которому ставится лайк.
     * @param userId Идентификатор пользователя, который ставит лайк.
     */
    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.addLike(filmId, userId);
    }

    /**
     * Обрабатывает DELETE-запрос, чтобы удалить лайк указанного пользователя у указанного фильма.
     *
     * @param filmId Идентификатор фильма, у которого удаляется лайк.
     * @param userId Идентификатор пользователя, который удаляет лайк.
     */
    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto deleteLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.deleteLike(filmId, userId);
    }

    /**
     * Обрабатывает GET-запрос для получения самых популярных фильмов на основе количества лайков.
     *
     * @param filmsCount количество возвращаемых популярных фильмов. Если не указано, по умолчанию равно 10.
     * @return Список самых популярных фильмов, ограниченный указанным количеством.
     */
    @GetMapping("/popular")
    public Collection<FilmDto> getTopFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer filmsCount) {
        return filmService.getTopFilms(filmsCount);
    }
}