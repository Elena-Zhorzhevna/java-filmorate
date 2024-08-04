package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;
import java.util.Optional;

@Log4j2
@Repository
public class RatingMpaDbStorage extends BaseDbStorage<RatingMpa> {
    private static final String SELECT_RATING_MPA = "SELECT * FROM rating_mpa WHERE rating_id = ?";
    private static final String SELECT_ALL_RATINGS = "SELECT * FROM rating_mpa";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    public RatingMpa getRatingMpa(int ratingId) {
        RatingMpa ratingMpa = findOne(SELECT_RATING_MPA, ratingId);
        if (ratingMpa != null) {
            return ratingMpa;
        } else {
            log.error("Рейтинг с ID = " + ratingId + " не найден");
            throw new NotFoundException("Рейтинг с данным id не найден");
        }
    }

    public Collection<RatingMpa> getAllRatings() {
        return findMany(SELECT_ALL_RATINGS);
    }
}