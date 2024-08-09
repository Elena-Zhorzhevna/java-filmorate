package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.db_storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса FilmStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbc;
    private static final String DELETE_FILMS_QUERY = "DELETE FROM films";
    Film testFilm = new Film();
    Film testFilm2 = new Film();

    @BeforeEach
    public void filmsForTest() {
        testFilm.setName("TestFilmName");
        testFilm.setDescription("Description");
        testFilm.setReleaseDate(LocalDate.parse("1946-08-20"));
        testFilm.setDuration(120);
        RatingMpa mpa = new RatingMpa();
        mpa.setId(3);
        testFilm.setMpa(mpa);

        testFilm2.setName("Приключения электроника");
        testFilm2.setDescription("Фильм о приключениях электроника");
        testFilm2.setReleaseDate(LocalDate.parse("1980-09-09"));
        testFilm2.setDuration(130);
        RatingMpa mpa2 = new RatingMpa();
        mpa2.setId(1);
        testFilm2.setMpa(mpa2);
    }

    @AfterEach
    public void afterEach() {
        jdbc.update(DELETE_FILMS_QUERY);
    }

    /**
     * Тест добавления фильма.
     */
    @Test
    void createFilmTest() {
        filmDbStorage.create(testFilm);
        assertThat(filmDbStorage.findFilmById(testFilm.getId()).getDescription()).isEqualTo(testFilm.getDescription());
        assertThat(filmDbStorage.findFilmById(testFilm.getId()).getName()).isEqualTo(testFilm.getName());
    }

    /**
     * Тест получения фильма по идентификатору.
     */
    @Test
    void findFilmByIdTest() {
        filmDbStorage.create(testFilm2);
        Film newTestFilm = filmDbStorage.findFilmById(testFilm2.getId());
        assertThat(newTestFilm.getName()).isEqualTo(testFilm2.getName());
        assertThat(newTestFilm.getDescription()).isEqualTo(testFilm2.getDescription());
        assertThat(newTestFilm.getReleaseDate()).isEqualTo(testFilm2.getReleaseDate());
        assertThat(newTestFilm.getDuration()).isEqualTo(testFilm2.getDuration());
        assertThat(newTestFilm.getMpa().getId()).isEqualTo(testFilm2.getMpa().getId());
    }

    /**
     * Тест получения всех фильмов.
     */
    @Test
    void findAllFilmsTest() {
        filmDbStorage.create(testFilm2);
        filmDbStorage.create(testFilm);
        assertThat(filmDbStorage.findAll().size()).isEqualTo(2);
        assertThat(filmDbStorage.findAll().stream().map(Film::getName).toList())
                .isEqualTo(List.of("Приключения электроника", "TestFilmName"));
    }

    /**
     * Тест удаления всех фильмов.
     */
    @Test
    void removeAllFilmsTest() {
        filmDbStorage.create(testFilm);
        filmDbStorage.create(testFilm2);
        assertThat(filmDbStorage.findAll().size()).isEqualTo(2);
        filmDbStorage.removeAllFilms();
        assertThat(filmDbStorage.findAll().size()).isEqualTo(0);
    }

    /**
     * Тест удаления фильма по идентификатору.
     */
    @Test
    void removeFilmByIdTest() {
        filmDbStorage.create(testFilm2);
        assertThat(filmDbStorage.findAll()).hasSize(1);
        filmDbStorage.delete(testFilm2.getId());
        assertThat(filmDbStorage.findAll()).hasSize(0);
    }

    /**
     * Тест обновления фильма.
     */
    @Test
    public void updateFilmTest() {
        filmDbStorage.create(testFilm2);
        testFilm2.setName("NewTestName");
        filmDbStorage.update(testFilm2);
        assertThat(filmDbStorage.findFilmById(testFilm2.getId()).getName()).isEqualTo("NewTestName");
    }
}