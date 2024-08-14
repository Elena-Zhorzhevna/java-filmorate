package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.RatingMpaDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.RatingMpaDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с рейтингами фильмов.
 * Во всех случаях возвращает объекты RatingMpaDto.
 */
@Service
public class RatingMpaService {

    private final RatingMpaDbStorage mpaDbStorage;

    public RatingMpaService(RatingMpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    /**
     * Получение всех рейтингов.
     *
     * @return Коллекция рейтингов.
     */
    public Collection<RatingMpaDto> getAllRatingsMpa() {
        return mpaDbStorage.getAllRatings().stream()
                .map(RatingMpaDtoMapper::mapToMpaDto)
                .sorted(Comparator.comparingInt(RatingMpaDto::getId))
                .collect(Collectors.toList());
    }

    /**
     * Получение рейтинга по идентификатору.
     *
     * @param id Идентификатор рейтинга.
     * @return Рейтинг с заданным идентификатором.
     */
    public RatingMpaDto getRatingMpa(int id) {
        return RatingMpaDtoMapper.mapToMpaDto(mpaDbStorage.getRatingMpa(id));
    }
}