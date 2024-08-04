package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.MpaDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.MpaDto;


import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
@Service
public class RatingMpaService {

    private final RatingMpaDbStorage mpaDbStorage;

    public RatingMpaService(RatingMpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<MpaDto> getAllRatingsMpa() {
        return mpaDbStorage.getAllRatings().stream()
                .map(MpaDtoMapper::mapToMpaDto)
                .sorted(Comparator.comparingInt(MpaDto::getId))
                .collect(Collectors.toList());
    }

    public MpaDto getRatingMpa(int id) {
        return MpaDtoMapper.mapToMpaDto(mpaDbStorage.getRatingMpa(id));
    }
}