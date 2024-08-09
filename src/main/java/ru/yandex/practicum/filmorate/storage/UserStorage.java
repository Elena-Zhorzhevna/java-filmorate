package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс, в котором определены методы добавления, удаления и модификации объектов User.
 */
public interface UserStorage {

    /**
     * Получение всех пользователей.
     */
    Collection<User> findAll();

    /**
     * Список Пользователей в таком же порядке, как и {@param ids}.
     * Не Содержит null.
     */
    Collection<User> findAll(Collection<Long> ids);

    /**
     * Добавление пользователя.
     */
    User create(User user);

    /**
     * Обновление пользователя.
     */
    User update(User newUser);

    /**
     * Получение пользователя по айди.
     */
    User findUserById(Long userId);

    /**
     * Удаление всех пользователей.
     */
    void removeAllUsers();
}