package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.GenreDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с жанрами.
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreStorage;

    /**
     *
     * @return
     */
    public Collection<GenreDto> getGenres() {
        return genreStorage.getAllGenres().stream()
                .map(GenreDtoMapper::mapToGenreDto)
                .sorted(Comparator.comparingInt(GenreDto::getId))
                .collect(Collectors.toList());
    }

    public GenreDto getGenre(int id) {
        return GenreDtoMapper.mapToGenreDto(genreStorage.findGenreById(id));
    }
}