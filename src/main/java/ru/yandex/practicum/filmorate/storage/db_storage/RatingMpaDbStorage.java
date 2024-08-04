package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.Collection;


@Log4j2
@Repository
public class RatingMpaDbStorage extends BaseDbStorage<Mpa> {
    private static final String SELECT_RATING_MPA = "SELECT * FROM rating_mpa WHERE rating_id = ?";
    private static final String SELECT_ALL_RATINGS = "SELECT * FROM rating_mpa";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Mpa getRatingMpa(int id) {
        Mpa Mpa = findOne(SELECT_RATING_MPA, id);
        if (Mpa != null) {
            return Mpa;
        } else {
            log.error("Рейтинг с ID = " + id + " не найден");
            throw new NotFoundException("Рейтинг с данным id не найден");
        }
    }

    public Collection<Mpa> getAllRatings() {
        return findMany(SELECT_ALL_RATINGS);
    }
}