package ru.yandex.practicum.filmorate.storage.dto.mapperDto;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

public class FilmDtoMapper {
    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setDuration(film.getDuration());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setLikes(film.getLikes());
        filmDto.setMpa(film.getMpa());
        filmDto.setGenres(film.getGenres());
        return filmDto;
    }
}