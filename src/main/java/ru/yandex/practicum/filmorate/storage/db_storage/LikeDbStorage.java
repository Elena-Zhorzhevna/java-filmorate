package ru.yandex.practicum.filmorate.storage.db_storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Является DAO — объектом доступа к данным лайка.
 */
@Repository
public class LikeDbStorage extends BaseDbStorage<Long> {
    private static final String INSERT_LIKE = "INSERT INTO film_likes(film_id, " +
            "user_id) VALUES(?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String SELECT_LIKES = "SELECT user_id FROM film_likes WHERE film_id = ?";


    public LikeDbStorage(JdbcTemplate jdbc) {
        super(jdbc, (rs, rowNum) -> rs.getLong("user_id"));
    }

    /**
     * Добавление лайка фильму, если его ещё нет, если есть, то не добавляется.
     */
    public void addLike(Long filmId, Long userId) {
        if (this.getLikes(filmId).contains(userId)) return;
        update(INSERT_LIKE, filmId, userId);
    }

    /**
     * Удаление лайка.
     */
    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE, filmId, userId);
    }

    /**
     * Получение всех лайков.
     */
    public Collection<Long> getLikes(Long filmId) {
        return findMany(SELECT_LIKES, filmId);
    }
}