package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс обрабатывает отдельно каждую запись, полученную из БД, и возвращает уже готовый объект — Friend.
 */
@Component
public class FriendRowMapper implements RowMapper<Friend> {

    @Override
    public Friend mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setId(resultSet.getInt("user_id"));
        friend.setId(resultSet.getInt("friend_id"));
        friend.setFriendshipStatus(resultSet.getInt("status_id"));
        return friend;
    }
}