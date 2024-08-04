package ru.yandex.practicum.filmorate.storage.db_storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;


@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
   // private static final String SELECT_ALL_FILM_GENRES_ID = "SELECT genre_id FROM genre WHERE film_id = ?";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM GENRE WHERE genre_id = ?";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genre ORDER BY genre_id";
    private static final String DELETE_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String INSERT_GENRE = "INSERT INTO film_genre (film_id, " +
            "genre_id) VALUES(?, ?)";
    private static final String GET_GENRES_BY_FILM_ID = "SELECT * FROM genres g " +
            "INNER JOIN film_genre fg on g.genre_id = fg.genre_id " +
            "WHERE film_id = ?";


    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre findGenreById(Integer id) {
       return findOne(SELECT_GENRE_BY_ID, id);
    }
    public Collection<Genre> getAllGenres() {
        return findMany(SELECT_ALL_GENRES);
    }

    public List<Genre> getFilmGenres(Long filmId) {
        return findMany(GET_GENRES_BY_FILM_ID, filmId);
    }
    public void addGenre(Long filmId, Integer genreId) {
        update(INSERT_GENRE, filmId, genreId);
    }

    public void deleteGenre(Long filmId, Long userId) {
        delete(DELETE_GENRE, filmId, userId);
    }
}