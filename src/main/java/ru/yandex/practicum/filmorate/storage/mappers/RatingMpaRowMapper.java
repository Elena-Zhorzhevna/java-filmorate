package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс обрабатывает отдельно каждую запись, полученную из БД, и возвращает уже готовый объект — RatingMpa.
 */
@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {

    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setId(rs.getInt("rating_id"));
        ratingMpa.setName(rs.getString("name"));
        return ratingMpa;
    }
}