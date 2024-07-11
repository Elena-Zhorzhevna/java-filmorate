package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll(); //получение всех пользователей

    User create(User user); //добавление пользователя

    User update(User newUser); //обновление пользователя

    User findUserById(Long userId); //получение пользователя по айди

    boolean isFriend(long filmId, long userId); //проверка, являются ли пользователи друзьями

    void removeAllUsers(); //удаление всех пользователей
}