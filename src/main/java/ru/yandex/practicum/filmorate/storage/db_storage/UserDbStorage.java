package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.List;

/**
 * Новая имплементация интерфейса UserStorage.
 * Является DAO — объектом доступа к данным пользователя.
 */
@Log4j2
@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private final UserRowMapper userRowMapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_ALL_WHERE_ID_IN_QUERY = "SELECT * FROM users WHERE user_id IN (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(login, email, name, birthday)" +
            "VALUES (?, ?, ?, ?)";

    private static final String INSERT_FRIENDS_QUERY = "INSERT INTO friends VALUES(?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ?" +
            " WHERE user_id = ?";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, UserRowMapper userRowMapper) {
        super(jdbc, mapper);
        this.userRowMapper = userRowMapper;
    }

    /**
     * Метод получения всех пользователей.
     *
     * @return Список пользователей.
     */
    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    /**
     * Метод получения коллекции пользователей по коллекции идентификаторов.
     *
     * @param ids Коллекция идентификаторов.
     * @return Коллекция пользователей.
     */
    @Override
    public Collection<User> findAll(Collection<Long> ids) {
        return findMany(FIND_ALL_WHERE_ID_IN_QUERY, super.collectionToSqlSyntax(ids));
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Пользователь с заданным идентификатором.
     */
    @Override
    public User findUserById(Long userId) {
        User user = findOne(FIND_BY_ID_QUERY, userId);
        if (user != null) {
            return user;
        } else {
            log.error("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    /**
     * Добавление пользователя
     *
     * @param user Добавляемый пользователь.
     * @return Добавленный пользователь.
     */
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
            update(INSERT_FRIENDS_QUERY, id, friend.getId());
        }
        user.setId(id);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    /**
     * Обновление пользователя.
     *
     * @param newUser Пользователь с обновленной информацией.
     * @return Обновленный пользователь.
     */
    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        UserValidator.isValidUser(newUser);
        User user = findOne(FIND_BY_ID_QUERY, newUser.getId());
        if (user != null) {
            update(
                    UPDATE_QUERY,
                    newUser.getLogin(),
                    newUser.getEmail(),
                    newUser.getName(),
                    newUser.getBirthday(),
                    newUser.getId()
            );
            return newUser;

        } else {
            log.error("Пользователь с id = " + newUser.getId() + " не найден");
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Результат удаления.
     */
    public boolean delete(long userId) {
        return delete(DELETE_USER_BY_ID_QUERY, userId);
    }

    /**
     * Удаление всех пользователей.
     */
    @Override
    public void removeAllUsers() {
        jdbc.update(DELETE_ALL_USERS_QUERY);
    }
}