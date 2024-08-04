package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.RatingMpaService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.MpaDto;

import java.util.Collection;

/**
 * Класс контроллера для управления рейтингами Ассоциации кинокомпаний
 * (англ. Motion Picture Association, сокращённо МРА).
 */
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMpaController {
    private final RatingMpaService mpaService;

    /**
     * Обрабатывает GET-запрос на получение всех рейтингов.
     * @return Коллекция всех рейтингов.
     */
    @GetMapping
    public Collection<MpaDto> getRatingsMpa() {
        return mpaService.getAllRatingsMpa();
    }

    /**
     * Обрабатывает GET - запрос на получение рейтинга по идентификатору.
     * @param id Идентификатор рейтинга.
     * @return Рейтинг с указанным идентификатором.
     */
    @GetMapping("/{id}")
    public MpaDto getRatingMpa(@PathVariable int id) {
        return mpaService.getRatingMpa(id);
    }
}