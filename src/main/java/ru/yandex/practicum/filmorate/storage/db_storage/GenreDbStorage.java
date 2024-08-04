package ru.yandex.practicum.filmorate.storage.db_storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;


@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String SELECT_GENRE = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genre ORDER BY genre_id";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre findGenreById(long id) {
       return findOne(SELECT_GENRE, id);
    }

    public Collection<Genre> getGenres() {
        return findMany(SELECT_ALL_GENRES);
    }
}