package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //получение всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return userService.getAllUsers();
    }

    //получение пользователя по id
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    //добавление пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.addUser(user);
    }

    //обновление пользователя
    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    //добавление в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable("userId") long userId, @PathVariable("friendId") long friendId) {
        return userService.addFriend(userId, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable("userId") long userId, @PathVariable("friendId") long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    //возвращение списка друзей пользователя
    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable long userId) {
        return userService.getUsersFriends(userId);
    }

    //получение списка общих друзей двух пользователей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}