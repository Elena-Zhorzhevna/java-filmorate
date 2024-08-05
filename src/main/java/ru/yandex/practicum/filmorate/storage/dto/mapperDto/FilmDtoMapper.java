package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.GenreDto;

import java.util.Comparator;
import java.util.stream.Collectors;

public class FilmDtoMapper {
    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setDuration(film.getDuration());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setLikes(film.getLikes());

        filmDto.setGenres(film.getGenres()
                .stream()
                .map(GenreDtoMapper::mapToGenreDto)
                .sorted(Comparator.comparing(GenreDto::getId))
                .collect(Collectors.toList()));
        if (film.getMpa() != null) {
            filmDto.setMpa(RatingMpaDtoMapper.mapToMpaDto(film.getMpa()));
        }
        return filmDto;
    }
}