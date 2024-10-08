package ru.yandex.practicum.filmorate.storage.db_storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

/**
 * Является DAO — объектом доступа к данным друга.
 */
@Repository
public class FriendDbStorage extends BaseDbStorage<Friend> {
    private static final String INSERT_FRIEND = "INSERT INTO friends(user_id, " +
            "friend_id, status_id) VALUES(?, ?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String SELECT_FRIENDS = "SELECT * FROM friends WHERE user_id = ?";

    public FriendDbStorage(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    /**
     * Добавление друга.
     *
     * @param userId   Идентификатор пользователя.
     * @param friendId Идентификатор друга.
     */
    public void addFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            throw new NotFoundException("Пользователь с таким айди не найден.");
        } else {
            update(INSERT_FRIEND, userId, friendId, 2);
        }
    }

    /**
     * Удаление друга.
     *
     * @param userId   Идентификатор пользователя.
     * @param friendId Идентификатор друга.
     */
    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND, userId, friendId);
    }

    /**
     * Получение всех друзей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список друзей.
     */
    public List<Friend> getFriends(Long userId) {
        return findMany(SELECT_FRIENDS, userId);
    }
}