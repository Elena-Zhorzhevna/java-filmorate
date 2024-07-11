package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //получение всех пользователей
    public Collection<User> getAllUsers() { //List?
        return userStorage.findAll();
    }

    //получение пользователя по айди
    public User getUserById(long id) {
        return userStorage.findUserById(id);
    }

    //добавление пользователя
    public User addUser(User user) {
        return userStorage.create(user);
    }

    //обновление пользователя
    public User updateUser(User user) {
        return userStorage.update(user);
    }

    //удаление всех пользователей
    public void removeAllUsers() {
        userStorage.removeAllUsers();
    }

    //метод добавления пользователя в друзья
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

    //метод удаления пользователя из друзей
    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователь " + friend.getName() + " удален из списка друзей " + user.getName());
        return user;
    }

    //метод выводит список общих друзей
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

    //метод выводит список друзей пользователя
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
