package ru.yandex.practicum.filmorate.storage.db_storage;


import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.validator.UserValidator;


import java.util.List;

@Log4j2
@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private final UserRowMapper userRowMapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(login, email, name, birthday)" +
            "VALUES (?, ?, ?, ?)";

    private static final String INSERT_FRIENDS_QUERY = "INSERT INTO friends VALUES(?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ?" +
            " WHERE user_id = ?";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "TRUNCATE TABLE users";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, UserRowMapper userRowMapper) {
        super(jdbc, mapper);
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User findUserById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User create(User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        UserValidator.isValidUser(user);
        long id = insert(
                INSERT_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday());
        for (Friend friend : user.getFriends()) {
            update(INSERT_FRIENDS_QUERY, id, friend.getFriendId());
        }
        user.setId(id);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        UserValidator.isValidUser(newUser);
            update(
                    UPDATE_QUERY,
                    newUser.getLogin(),
                    newUser.getEmail(),
                    newUser.getName(),
                    newUser.getBirthday(),
                    newUser.getId()
                    //newUser.getFriends()
            );

            log.info("Пользователь с id = " + newUser.getId() + " не найден");

        log.info("Пользователь после обновления: {}", newUser);
        return newUser;
    }


    public boolean delete(long userId) {
        return delete(DELETE_USER_BY_ID_QUERY, userId);
    }

    @Override
    public void removeAllUsers() {
        jdbc.update(DELETE_ALL_USERS_QUERY);
    }
}
