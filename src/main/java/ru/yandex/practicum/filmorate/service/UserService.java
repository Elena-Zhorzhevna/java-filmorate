package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с пользователями.
 */
@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Получение всех пользователей.
     *
     * @return Коллекция пользователей.
     */
    public Collection<User> getAllUsers() {
        return userStorage.findAll();
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с заданным идентификатором.
     */
    public User getUserById(long id) {
        return userStorage.findUserById(id);
    }

    /**
     * Добавление пользователя.
     *
     * @param user Добавляемый пользователь.
     * @return Добавленный пользователь.
     */
    public User addUser(User user) {
        return userStorage.create(user);
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param user Пользователь с обновленными данными.
     * @return Обновленный пользователь.
     */
    public User updateUser(User user) {
        return userStorage.update(user);
    }

    /**
     * Удаление всех пользователей.
     */
    public void removeAllUsers() {
        userStorage.removeAllUsers();
    }

    /**
     * Добавление пользователя в друзья.
     *
     * @param userId   Идентификатор пользователя, который добавляет друга.
     * @param friendId Идентификатор добавляемого друга.
     * @return Обновленные данные пользователя.
     */
    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        if (userStorage.isFriend(userId, friendId)) {
            throw new IllegalArgumentException("Пользователи уже являются друзьями.");
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователь " + friend.getName() + " добавлен в список друзей " + user.getName());
        return user;
    }

    /**
     * Удаление пользователя из друзей.
     *
     * @param userId   Идентификатор пользователя, который хочет удалить друга.
     * @param friendId Идентификатор друга, который должан быть удален.
     * @return Обновленные данные пользователя.
     */
    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователь " + friend.getName() + " удален из списка друзей " + user.getName());
        return user;
    }

    /**
     * Получение списка общих друзей пользователей.
     *
     * @param userId   Идентификатор первого пользователя.
     * @param friendId Идентификатор второго пользователя.
     * @return Список общих друзей двух пользователей.
     */
    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        List<User> commonFriendsList = new ArrayList<>();
        for (Long id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                commonFriendsList.add(userStorage.findUserById(id));
            }
        }
        log.info("Список общих друзей пользователей {} и {}", user.getName(), friend.getName());
        return commonFriendsList;
    }

    /**
     * Получение друзей пользователя, чей идентификатор указан.
     *
     * @param userId Идентификатор пользователя, чьи друзья должны быть получены.
     * @return Список друзей пользователя.
     */
    public List<User> getUsersFriends(Long userId) {
        User user = userStorage.findUserById(userId);
        List<User> friendsList = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friendsList.add(userStorage.findUserById(id));
        }
        log.info("Список друзей пользователя " + user.getName());
        return friendsList;
    }
}