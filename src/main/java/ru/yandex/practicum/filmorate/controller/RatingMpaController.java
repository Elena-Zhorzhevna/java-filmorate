package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.RatingMpaService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.RatingMpaDto;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMpaController {
    private final RatingMpaService mpaService;

    @GetMapping
    public Collection<RatingMpaDto> getRatingsMpa() {
        return mpaService.getAllRatingsMpa();
    }

    @GetMapping("/{ratingId}")
    public RatingMpaDto getRatingMpa(@PathVariable int ratingId) {
        return mpaService.getRatingMpa(ratingId);
    }
}