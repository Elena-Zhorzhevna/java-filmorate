package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.RatingMpaDto;

public class RatingMpaDtoMapper {
    public static RatingMpaDto mapToMpaDto(RatingMpa ratingMpa) {
        RatingMpaDto ratingMpaDto = new RatingMpaDto();
        ratingMpaDto.setRatingId(ratingMpa.getRatingId());
        ratingMpaDto.setName(ratingMpa.getRatingName());
        return ratingMpaDto;
    }
}
