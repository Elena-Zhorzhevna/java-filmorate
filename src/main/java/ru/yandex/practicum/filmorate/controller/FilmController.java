package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAllFilms();
    }

    //получение фильма по id
    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") long filmId) {
        return filmService.getFilmById(filmId);
    }

    //добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    //добавление лайка фильму
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
    }

    //удаление лайка у фильма
    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    //если значение параметра count не задано, возвращает первые 10
    @GetMapping("/popular")
    public List<Film> getTopFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer filmsCount) {
        return filmService.getTopFilms(filmsCount);
    }
}