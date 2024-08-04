package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class FilmControllerTest {
    @Autowired
    FilmService filmService;
    Map<Long, Film> films = new HashMap<>();

    @AfterEach
    void afterEach() {
        films.clear();
        filmService.removeAllFilms();
    }

    /**
     * Тест получения всех фильмов.
     */
    @Test
    void findAllFilmsTest() {
        Film film1 = new Film("testFilm1", "Comedy",
                LocalDate.of(1995, Month.MAY, 12), 180);
        films.put(film1.getId(), film1);
        filmService.createFilm(film1);
        Film film2 = new Film("testFilm2", "Triller",
                LocalDate.of(1998, Month.APRIL, 20), 200);
        films.put(film2.getId(), film2);
        filmService.createFilm(film2);
        String result = "[" + film1.toString() + ", " + film2.toString() + "]";
        String expected = filmService.getAllFilms().toString();
        assertEquals(expected, result);
    }

    /**
     * Тест добавления фильма.
     */
    @Test
    void filmCreationTest() {
        String name = "TestFilmForCreation";
        String description = "TestDescription";
        LocalDate releaseDate = LocalDate.of(2000, Month.MAY, 1);
        int duration = 203;
        Film film = new Film(name, description, releaseDate, duration);
        try {
            films.put(film.getId(), film);
            assertEquals(film.getId(), films.get(film.getId()).getId());
        } catch (ValidationException e) {
            fail("ValidationException was thrown");
        }
    }

    /**
     * Тест обновления существующего фильма.
     */
    @Test
    public void updateExistingFilmTest() {
        String name = "TestFilmToUpdate";
        String description = "TestDescription";
        LocalDate releaseDate = LocalDate.of(2013, Month.APRIL, 1);
        int duration = 135;
        Film existingFilm = new Film(name, description, releaseDate, duration);
        filmService.createFilm(existingFilm);
        Film newFilm = new Film(existingFilm.getId(), "NewName", "NewDescription", releaseDate, 90);
        films.put(newFilm.getId(), newFilm);
        try {
            FilmDto updatedFilm = filmService.updateFilm(newFilm);
            assertEquals(updatedFilm.getName(), "NewName");
            assertEquals(updatedFilm.getDescription(), "NewDescription");
            assertEquals(updatedFilm.getReleaseDate(), releaseDate);
            assertEquals(updatedFilm.getDuration(), 90);
        } catch (NotFoundException e) {
            fail("NotFoundException was thrown");
        }
    }

    /**
     * Тест обновления несуществующего фильма.
     */
    @Test
    void nonExistingFilmUpdateTest() {
        Film nonExistingFilm = new Film("name", "description", LocalDate.now(), 90);
        final Exception actualException = Assertions.assertThrows(NotFoundException.class,
                () -> {
                    filmService.updateFilm(nonExistingFilm);
                });
    }

    /**
     * Создание фильма с пустым полем name.
     */
    @Test
    void nameIsBlankTest() {
        String expectedExceptionMessage = "Название фильма не может быть пустым.";
        Film film = new Film(" ", "description",
                LocalDate.parse("1995-12-27"), 60);
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    filmService.createFilm(film);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Создание фильма с описанием длиннее 200 символов.
     */
    @Test
    void descriptionIsMore200Test() {
        String expectedExceptionMessage = "Максимальная длина описания - 200 символов.";
        Film film = new Film("name", "descriptiondescriptiondescripti" +
                "ondescriptiondescriptiondescriptiondescriptiondescriptiondescripti" +
                "ondescriptiondescriptiondescriptiondescriptiondescriptiondescriptio" +
                "ndescriptiondescriptiondescription201", LocalDate.of(2005, Month.NOVEMBER, 11),
                132);
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    filmService.createFilm(film);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Создание фильма с датой релиза раньше даты создания кино.
     */
    @Test
    void dataIsBeforeFilmsBirthdayTest() {
        String expectedExceptionMessage = "Дата релиза не может быть раньше создания кино.";
        Film film = new Film("name", "description",
                LocalDate.parse("1895-12-27"), 60);
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    filmService.createFilm(film);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Создание фильма с датой релиза позже даты создания кино.
     */
    @Test
    void dataIsAfterFilmsBirthdayTest() {
        Film film = new Film("name", "description",
                LocalDate.parse("1895-12-29"), 60);
        filmService.createFilm(film);
        String expected = filmService.createFilm(film).toString();
        assertEquals(expected, film.toString());
    }

    /**
     * Создание фильма с продолжительностью меньше 0.
     */
    @Test
    void durationIsLess0() {
        String expectedExceptionMessage = "Продолжительность фильма должна быть положительным числом.";
        Film film = new Film("name", "description",
                LocalDate.parse("2005-11-25"), -7);
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    filmService.createFilm(film);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Создание фильма с продолжительностью больше 0.
     */
    @Test
    void durationIsMore0() {
        Film film = new Film("name", "description",
                LocalDate.parse("1995-10-25"), 50);
        filmService.createFilm(film);
        String result = "[" + film.toString() + "]";
        String expected = filmService.getAllFilms().toString();
        assertEquals(expected, result);
    }
}