package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

public class GenreDtoMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setGenreId(genre.getGenreId());
        genreDto.setName(genre.getName());
        return genreDto;
    }
}
