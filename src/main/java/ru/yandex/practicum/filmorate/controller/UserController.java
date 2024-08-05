package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.UserDto;

import java.util.Collection;
import java.util.Optional;

/**
 * Класс контроллера для управления пользователями в приложении Filmorate.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обрабатывает GET-запросы для получения всех пользователей.
     *
     * @return Коллекция всех пользователей.
     */
    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.getAllUsers();
    }

    /**
     * Обрабатывает GET-запросы для получения пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором.
     */
    @GetMapping("/{userId}")
    public Optional<UserDto> getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    /**
     * Обрабатывает POST-запросы для добавления пользователя.
     *
     * @param user Пользователь, который должен быть добавлен.
     * @return Добавляемый пользователь.
     */
    @PostMapping
    public UserDto create(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Обрабатывает PUT-запросы на обновление существующего пользователя.
     *
     * @param newUser Пользователь с обновленной информацией.
     * @return Обновленный пользователь.
     */
    @PutMapping
    public UserDto update(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    /**
     * Обрабатывает PUT-запрос на добавление пользователя в друзья другому пользователю с указанными инентификаторами.
     *
     * @param userId   Идентификатор пользователя, который добавляет друга.
     * @param friendId Идентификатор добавляемого друга.
     * @return Обновленные данные пользователя.
     */
    @PutMapping("/{userId}/friends/{friendId}")
    public UserDto addFriend(@PathVariable("userId") long userId, @PathVariable("friendId") long friendId) {

        /* Валидация, есть такие пользователи или нет - если нет, то упадём с NotFoundException -> 404 */
        // Todo можно сделать красивее и понятнее
        userService.getUserById(userId);
        userService.getUserById(friendId);

        return userService.addFriend(userId, friendId);
    }

    /**
     * Обрабатывает DELETE-запрос для удаления указанного друга из списка друзей пользователя.
     *
     * @param userId   Идентификатор пользователя, который хочет удалить друга.
     * @param friendId Идентификатор друга, который должан быть удален.
     * @return Обновленные данные пользователя.
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable("userId") long userId, @PathVariable("friendId") long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    /**
     * Обрабатывает GET-запросы для поиска друзей пользователя, чей идентификатор указан.
     *
     * @param userId Идентификатор пользователя, чьи друзья должны быть получены.
     * @return Список друзей пользователя.
     */
    @GetMapping("/{userId}/friends")
    public Collection<UserDto> getFriends(@PathVariable long userId) {
        return userService.getUsersFriends(userId);
    }

    /**
     * Обрабатывает GET-запрос для получения списка общих друзей двух пользователей.
     *
     * @param userId  Идентификатор первого пользователя.
     * @param otherId Идентификатор второго пользователя.
     * @return Список общих друзей двух пользователей.
     */
    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<UserDto> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}