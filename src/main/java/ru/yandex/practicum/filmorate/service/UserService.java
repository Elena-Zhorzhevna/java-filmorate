package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.UserDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с пользователями.
 */
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendDbStorage friendDbStorage;
    private final ApplicationContext applicationContext; // для self injection

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendDbStorage friendDbStorage, WebApplicationContext applicationContext) {
        this.userStorage = userStorage;
        this.friendDbStorage = friendDbStorage;
        this.applicationContext = applicationContext;
    }

    /**
     * Получение всех пользователей.
     *
     * @return Коллекция пользователей.
     */
    public List<UserDto> getAllUsers() {
        return userStorage.findAll()
                .stream()
                .map(UserDtoMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Пользователь с заданным идентификатором.
     */
    public Optional<UserDto> getUserById(long userId) {
        User user = userStorage.findUserById(userId);
        user.setFriends(new HashSet<>(friendDbStorage.getFriends(userId)));
        return Optional.of(UserDtoMapper.mapToUserDto(user));
    }

    /**
     * Добавление пользователя.
     *
     * @param user Добавляемый пользователь.
     * @return Добавленный пользователь.
     */
    public UserDto addUser(User user) {
        return UserDtoMapper.mapToUserDto(userStorage.create(user));
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param user Пользователь с обновленными данными.
     * @return Обновленный пользователь.
     */
    public UserDto updateUser(User user) {
        return UserDtoMapper.mapToUserDto(userStorage.update(user));
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
    public UserDto addFriend(Long userId, Long friendId) {
        friendDbStorage.addFriend(userId, friendId);
        log.info("Пользователь с ID = {} добавил в друзья пользователя с ID = {}", userId, friendId);
        return self().getUserById(userId).orElseThrow();
    }

    /**
     * Удаление пользователя из друзей.
     *
     * @param userId   Идентификатор пользователя, который хочет удалить друга.
     * @param friendId Идентификатор друга, который должан быть удален.
     * @return Обновленные данные пользователя.
     */

    public UserDto removeFriend(long userId, long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        friendDbStorage.deleteFriend(userId, friendId);
        log.info("Пользователь с id = {} удалил из друзей пользователя с id = {}", userId, friendId);
        return UserDtoMapper.mapToUserDto(userStorage.findUserById(userId));
    }

    /**
     * Получение списка общих друзей пользователей.
     *
     * @param userId   Идентификатор первого пользователя.
     * @param otherId Идентификатор второго пользователя.
     * @return Список общих друзей двух пользователей.
     */
    public Collection<UserDto> getCommonFriends(long userId, long otherId) {
        List<Friend> userFriends = friendDbStorage.getFriends(userId);
        Set<Long> otherFriendIds = friendDbStorage.getFriends(otherId).stream().map(Friend::getId).collect(Collectors.toSet());

        Set<Long> commonFriendIds = userFriends.stream()
                .map(Friend::getId)
                .filter(otherFriendIds::contains)
                .collect(Collectors.toSet());

        return userStorage.findAll(commonFriendIds).stream()
                .map(UserDtoMapper::mapToUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }
    /*
    public Collection<UserDto> getCommonFriends(Long userId, Long otherId) {
           Set<Friend> userFriends = userStorage.findUserById(userId).getFriends();
       log.info("Список общих друзей пользователей {} и {}", userStorage.findUserById(userId).getName(),
                userStorage.findUserById(friendId).getName());
        return userFriends.stream()
                .filter(userStorage.findUserById(friendId).getFriends()::contains)
                .map(Friend::getFriendId)
                .map(userStorage::findUserById)
                .map(UserDtoMapper::mapToUserDto)
                .collect(Collectors.toList());
    }*/

    /**
     * Получение друзей пользователя, чей идентификатор указан.
     *
     * @param userId Идентификатор пользователя, чьи друзья должны быть получены.
     * @return Список друзей пользователя.
     */
    public Collection<UserDto> getUsersFriends(Long userId) {
        log.info("Список друзей пользователя " + userStorage.findUserById(userId).getName());
        return friendDbStorage.getFriends(userId).stream()
                .map(Friend::getId)
                .map(userStorage::findUserById)
                .map(UserDtoMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserService self() {
        return applicationContext.getBean(UserService.class);
    }
}