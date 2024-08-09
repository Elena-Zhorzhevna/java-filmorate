package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.RatingMpaDto;

/**
 * Класс для преобразования объектов типа RatingMpa в тип RatingMpaDto.
 */
public class RatingMpaDtoMapper {
    public static RatingMpaDto mapToMpaDto(RatingMpa ratingMpa) {
        RatingMpaDto ratingMpaDto = new RatingMpaDto();
        ratingMpaDto.setId(ratingMpa.getId());
        ratingMpaDto.setName(ratingMpa.getName());
        return ratingMpaDto;
    }
}