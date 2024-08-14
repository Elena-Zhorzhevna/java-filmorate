package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

import java.util.Collection;

/**
 * Класс контроллера для управления жанрами в приложении Filmorate.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    /**
     * Обрабатывает GET-запрос на получение всех жанров.
     *
     * @return Коллекция всех жанров.
     */
    @GetMapping
    public Collection<GenreDto> getFilmGenres() {
        return genreService.getGenres();
    }

    /**
     * Обрабатывает GET - запрос на получение жанра по идентификатору.
     *
     * @param id Идентификатор жанра фильма.
     * @return Жанр с указанным идентификатором.
     */
    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable int id) {
        return genreService.getGenre(id);
    }
}