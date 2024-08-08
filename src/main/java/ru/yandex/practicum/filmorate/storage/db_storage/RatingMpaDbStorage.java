package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;

/**
 * Является DAO — объектом доступа к данным рейтинга фильма.
 */
@Log4j2
@Repository
public class RatingMpaDbStorage extends BaseDbStorage<RatingMpa> {
    private static final String TABLE_NAME = "rating_mpa";
    private static final String ID_COLUMN_NAME = "rating_id";

    private static final String SELECT_RATING_MPA = "SELECT * FROM rating_mpa WHERE rating_id = ?";
    private static final String SELECT_ALL_RATINGS = "SELECT * FROM rating_mpa";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    /**
     * Получение рейтинга по идентификатору.
     *
     * @param id Идентификатор рейтинга.
     * @return Рейтинг с указанным идентификатором.
     */
    public RatingMpa getRatingMpa(int id) {
        RatingMpa mpa = findOne(SELECT_RATING_MPA, id);
        if (mpa != null) {
            return mpa;
        } else {
            log.error("Рейтинг с id = " + id + " не найден");
            throw new NotFoundException("Рейтинг с данным id не найден");
        }
    }

    /**
     * Получение всех рейтингов.
     *
     * @return Коллекция рейтингов.
     */
    public Collection<RatingMpa> getAllRatings() {
        return findMany(SELECT_ALL_RATINGS);
    }
}