package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс обрабатывает отдельно каждую запись, полученную из БД, и возвращает уже готовый объект — Film.
 */
@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setId(resultSet.getInt("rating_id"));
        film.setMpa(ratingMpa);
        return film;
    }
}