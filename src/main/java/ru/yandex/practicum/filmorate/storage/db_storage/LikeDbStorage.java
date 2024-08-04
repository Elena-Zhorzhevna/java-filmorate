package ru.yandex.practicum.filmorate.storage.db_storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class LikeDbStorage extends BaseDbStorage<Long> {
    private static final String INSERT_LIKE = "INSERT INTO film_likes(film_id, " +
            "user_id) VALUES(?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String SELECT_LIKES = "SELECT user_id FROM film_likes WHERE film_id = ?";

    //public LikeDbStorage(JdbcTemplate jdbc, RowMapper<Long> mapper) {
    //    super(jdbc, mapper);
   // }

       public LikeDbStorage(JdbcTemplate jdbc) {
           super(jdbc, (rs, rowNum) -> rs.getLong("user_id"));
       }

    public void addLike(Long filmId, Long userId) {
        update(INSERT_LIKE, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE, filmId, userId);
    }

    public Collection<Long> getLikes(Long filmId) {
        return findMany(SELECT_LIKES, filmId);
    }
}