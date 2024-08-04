package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

    @Component
    public class RatingMpaRowMapper implements RowMapper<Mpa> {

        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa ratingMpa = new Mpa();
            ratingMpa.setId(rs.getInt("rating_id"));
            ratingMpa.setName(rs.getString("name"));
            return ratingMpa;
        }
    }