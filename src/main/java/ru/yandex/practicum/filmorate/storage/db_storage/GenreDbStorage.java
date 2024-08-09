package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

/**
 * Является DAO — объектом доступа к данным жанра.
 */
@Log4j2
@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String TABLE_NAME = "genre";
    private static final String ID_COLUMN_NAME = "genre_id";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genre ORDER BY genre_id";
    private static final String DELETE_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String INSERT_GENRE = "INSERT INTO film_genre (film_id, " +
            "genre_id) VALUES(?, ?)";
    private static final String GET_GENRES_BY_FILM_ID = "SELECT * FROM genre g " +
            "INNER JOIN film_genre fg on g.genre_id = fg.genre_id " +
            "WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    /**
     * Получение жанра по идентификатору.
     *
     * @param id Идентификатор жанра.
     * @return Жанр с указанным идентификатором.
     */
    public Genre findGenreById(Integer id) {
        Genre genre = findOne(SELECT_GENRE_BY_ID, id);
        if (genre != null) {
            return genre;
        } else {
            log.error("Жанр с id = {} не найден", id);
            throw new NotFoundException("Жанр с данным id не найден");
        }
    }

    /**
     * Получение всех жанров.
     *
     * @return Коллекция жанров.
     */
    public Collection<Genre> getAllGenres() {
        return findMany(SELECT_ALL_GENRES);
    }

    /**
     * Получение жанров фильма с указанным идентификатором.
     *
     * @param filmId Идентификатор фильма.
     * @return Список жанров указанного фильма.
     */
    public List<Genre> getFilmGenres(Long filmId) {
        return findMany(GET_GENRES_BY_FILM_ID, filmId);
    }

    /**
     * Добавление жанра фильму, если его ещё нет.
     */
    public void addGenre(Long filmId, Integer genreId) {
        boolean hasFilmThisGenreOrNot = this.getFilmGenres(filmId).stream().anyMatch(it -> it.getId() == genreId);
        if (hasFilmThisGenreOrNot) return;
        update(INSERT_GENRE, filmId, genreId);
    }

    /**
     * Удаление жанра у указанного фильма.
     *
     * @param filmId Идентификатор фильма.
     */
    public void deleteGenre(Long filmId) {
        delete(DELETE_GENRE, filmId);
    }

    /**
     * Метод проверки наличия жанра у фильма.
     */
    public boolean contains(Long genreId) {
        return super.contains(TABLE_NAME, ID_COLUMN_NAME, genreId);
    }
}