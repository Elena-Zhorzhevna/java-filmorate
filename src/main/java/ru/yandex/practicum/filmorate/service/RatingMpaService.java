package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.RatingMpaDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.RatingMpaDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
@Service
public class RatingMpaService {

    private final RatingMpaDbStorage mpaDbStorage;

    public RatingMpaService(RatingMpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<RatingMpaDto> getAllRatingsMpa() {
        return mpaDbStorage.getAllRatings().stream()
                .map(RatingMpaDtoMapper::mapToMpaDto)
                .sorted(Comparator.comparingInt(RatingMpaDto::getRatingId))
                .collect(Collectors.toList());
    }

    public RatingMpaDto getRatingMpa(int ratingId) {
        return RatingMpaDtoMapper.mapToMpaDto(mpaDbStorage.getRatingMpa(ratingId));
    }
}