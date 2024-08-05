package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Log4j2
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, " +
            "rating_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ? WHERE film_id = ?";

    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "TRUNCATE TABLE films";
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private RatingMpaDbStorage ratingMpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film findFilmById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        FilmValidator.isValidFilm(film);
        RatingMpa mpa = film.getMpa();
        if (mpa == null) {
            log.error("Рейтинг mpa не найден.");
            throw new NotFoundException("Рейтинг mpa не найден.");
        }
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        FilmValidator.isValidFilm(newFilm);
        try {
            update(
                    UPDATE_QUERY,
                    newFilm.getName(),
                    newFilm.getDescription(),
                    newFilm.getReleaseDate(),
                    newFilm.getDuration(),
                    newFilm.getId()
            );
        } catch (NotFoundException e) {
            log.info("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.info("Фильм после обновления: {}", newFilm);
        return newFilm;
    }

    public boolean delete(long filmId) {
        log.info("Получен запрос на удаление фильма с айди: {}", filmId);
        return delete(DELETE_FILM_BY_ID_QUERY, filmId);
    }

    @Override
    public void removeAllFilms() {
        log.info("Получен запрос на удаление всех фильмов");
        jdbc.update(DELETE_ALL_FILMS_QUERY);
    }

    private void addGenres(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genres g, films_genre fg WHERE fg.id = g.id AND fg.film_id IN (" + filmIds + ")";
        jdbc.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            Genre genre = new Genre();
            genre.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
            film.addGenre(film.getId(), genre);
        });
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        String qs = "SELECT * FROM genres g " +
                "INNER JOIN film_genre fg on g.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        return jdbc.query(qs, this::makeGenre, filmId);
    }
    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}


/*
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Log4j2
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, " +
            "rating_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ? WHERE film_id = ?";

    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "TRUNCATE TABLE films";
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private RatingMpaDbStorage ratingMpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film findFilmById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        FilmValidator.isValidFilm(film);
        RatingMpa mpa = film.getMpa();
        if (mpa == null) {
            log.error("Рейтинг mpa не найден.");
            throw new NotFoundException("Рейтинг mpa не найден.");
        }
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        FilmValidator.isValidFilm(newFilm);
        try {
            update(
                    UPDATE_QUERY,
                    newFilm.getName(),
                    newFilm.getDescription(),
                    newFilm.getReleaseDate(),
                    newFilm.getDuration(),
                    newFilm.getId()
            );
        } catch (NotFoundException e) {
            log.info("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.info("Фильм после обновления: {}", newFilm);
        return newFilm;
    }

    public boolean delete(long filmId) {
        log.info("Получен запрос на удаление фильма с айди: {}", filmId);
        return delete(DELETE_FILM_BY_ID_QUERY, filmId);
    }

    @Override
    public void removeAllFilms() {
        log.info("Получен запрос на удаление всех фильмов");
        jdbc.update(DELETE_ALL_FILMS_QUERY);
    }*/

/*    private void addGenres(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genres g, films_genre fg WHERE fg.id = g.id AND fg.film_id IN (" + filmIds + ")";
        jdbc.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            Genre genre = new Genre();
            genre.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
            film.addGenre(film.getId(), genre);
        });
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        String qs = "SELECT * FROM genres g " +
                "INNER JOIN film_genre fg on g.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        return jdbc.query(qs, this::makeGenre, filmId);
    }
    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }*/


/*
package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;

@Log4j2
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, " +
            "rating_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ? WHERE film_id = ?";

    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "TRUNCATE TABLE films";
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private RatingMpaDbStorage ratingMpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film findFilmById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        FilmValidator.isValidFilm(film);
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        FilmValidator.isValidFilm(newFilm);
        try {
            update(
                    UPDATE_QUERY,
                    newFilm.getName(),
                    newFilm.getDescription(),
                    newFilm.getReleaseDate(),
                    newFilm.getDuration(),
                    //newFilm.getRatingMpa().getRatingId(),
                    newFilm.getId()
            );
        } catch (NotFoundException e) {
            log.info("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.info("Фильм после обновления: {}", newFilm);
        return newFilm;
    }

    public boolean delete(long filmId) {
        log.info("Получен запрос на удаление фильма с айди: {}", filmId);
        return delete(DELETE_FILM_BY_ID_QUERY, filmId);
    }

    @Override
    public void removeAllFilms() {
        log.info("Получен запрос на удаление всех фильмов");
        jdbc.update(DELETE_ALL_FILMS_QUERY);
    }
}*/
