package ru.yandex.practicum.filmorate.storage.dto.modelDto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class FilmDto {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes = new HashSet<>();
    //    private Set<Genre> genres = new HashSet<>();
//    private RatingMpa ratingMpa;
    private List<GenreDto> genres = new ArrayList<>();
    private RatingMpaDto mpa;
}

/*
@Data
public class FilmDto {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}*/
