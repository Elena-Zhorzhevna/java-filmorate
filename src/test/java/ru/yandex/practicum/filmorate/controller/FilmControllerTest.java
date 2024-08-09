package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmService filmService;
    Film testFilm = new Film();
    Film testFilm2 = new Film();

    @BeforeEach
    public void beforeEach() {
        testFilm.setName("ControllerTestFilm");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.parse("1965-12-12"));
        testFilm.setDuration(150);
        RatingMpa mpa = new RatingMpa();
        mpa.setId(1);
        testFilm.setMpa(mpa);

        testFilm2.setName("ControllerTestFilm2");
        testFilm2.setDescription("Desc2");
        testFilm2.setReleaseDate(LocalDate.parse("1966-12-12"));
        testFilm2.setDuration(90);
        RatingMpa mpa2 = new RatingMpa();
        mpa2.setId(4);
        testFilm2.setMpa(mpa2);
    }

    @AfterEach
    void afterEach() {
        filmService.removeAllFilms();
    }

    /**
     * Тест получения всех фильмов.
     */
    @Test
    void findAllFilmsTest() {
        filmService.createFilm(testFilm);
        filmService.createFilm(testFilm2);
        assertThat(filmService.getAllFilms().size()).isEqualTo(2);
        assertThat(filmService.getAllFilms().stream().map(FilmDto::getName).toList())
                .isEqualTo(List.of("ControllerTestFilm", "ControllerTestFilm2"));
    }

    /**
     * Тест добавления фильма.
     */
    @Test
    void filmCreationTest() {
        Film testCreationFilm = new Film(testFilm.getName(), testFilm.getDescription(), testFilm.getReleaseDate(),
                testFilm.getDuration(), testFilm.getMpa());
        filmService.createFilm(testCreationFilm);
        assertThat(filmService.getFilmById(testCreationFilm.getId()).getName()).isEqualTo("ControllerTestFilm");
        assertThat(filmService.getFilmById(testCreationFilm.getId()).getDescription()).isEqualTo("Description");
        assertThat(filmService.getFilmById(testCreationFilm.getId()).getReleaseDate())
                .isEqualTo("1965-12-12");
        assertThat(filmService.getFilmById(testCreationFilm.getId()).getDuration()).isEqualTo(150);
    }

    /**
     * Тест обновления существующего фильма.
     */
    @Test
    public void updateExistingFilmTest() {
        FilmDto existingFilm = filmService.createFilm(testFilm);
        Film newFilm = new Film(existingFilm.getId(), "NewName", "NewDescription",
                LocalDate.parse("2000-10-10"), 90);
        try {
            FilmDto updatedFilm = filmService.updateFilm(newFilm);
            assertThat(updatedFilm.getName()).isEqualTo("NewName");
            assertThat(updatedFilm.getDescription()).isEqualTo("NewDescription");
            assertThat(updatedFilm.getReleaseDate()).isEqualTo("2000-10-10");
            assertThat(updatedFilm.getDuration()).isEqualTo(90);
        } catch (NotFoundException e) {
            fail("NotFoundException was thrown");
        }
    }

    /**
     * Тест обновления несуществующего фильма.
     */
    @Test
    void nonExistingFilmUpdateTest() {
        Film nonExistingFilm = new Film(testFilm.getId(), "NonExistingFilmName", "description",
                LocalDate.parse("1990-12-12"), 90);
        final Exception actualException = Assertions.assertThrows(InternalServerException.class,
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
     * Создание фильма с описанием длиннее 200символов.
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
                LocalDate.parse("1895-12-29"), 60, new RatingMpa(1, "G"));
        FilmDto newFilm = filmService.createFilm(film);
        assertThat(newFilm.getName()).isEqualTo(film.getName());
        assertThat(newFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(newFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(newFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
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
        assertThat(expectedExceptionMessage).isEqualTo(actualException.getMessage());
    }

    /**
     * Создание фильма с продолжительностью больше 0.
     */
    @Test
    void durationIsMore0() {
        Film film = new Film("name", "description",
                LocalDate.parse("1995-10-25"), 50, new RatingMpa(1, "G"));
        filmService.createFilm(film);
        assertThat(filmService.getAllFilms().size()).isEqualTo(1);
        String result = "[FilmDto(id=1, name=name, description=description, releaseDate=1995-10-25, duration=50, " +
                "likes=[], genres=[], mpa=RatingMpaDto(id=1, name=null))]";
        String expected = filmService.getAllFilms().toString();
        assertThat(expected).isEqualTo(result);
    }
}