package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

/**
 * Класс для преобразования объектов типа Genre в тип GenreDto.
 */
public class GenreDtoMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }
}
