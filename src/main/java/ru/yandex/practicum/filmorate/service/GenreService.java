package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.GenreDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с жанрами.
 * Во всех случаях возвращает объекты GenreDto.
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreStorage;

    /**
     * Получение всех жанров.
     *
     * @return Коллекция жанров.
     */
    public Collection<GenreDto> getGenres() {
        return genreStorage.getAllGenres().stream()
                .map(GenreDtoMapper::mapToGenreDto)
                .sorted(Comparator.comparingInt(GenreDto::getId))
                .collect(Collectors.toList());
    }

    /**
     * Получение жанра по идентификатору.
     *
     * @param id Идентификатор жанра.
     * @return Жанр с заданным идентификатором.
     */
    public GenreDto getGenre(int id) {
        return GenreDtoMapper.mapToGenreDto(genreStorage.findGenreById(id));
    }
}