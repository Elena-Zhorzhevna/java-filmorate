package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Long, User> users = new HashMap<>();

    //получение всех пользователей
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    //добавление пользователя
    @Override
    public User create(User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        UserValidator.isValidUser(user);
        user.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    //обновление пользователя
    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.debug("Пользователь до обновления: {}", oldUser);
            // если пользователь найден и все условия соблюдены, обновляем её содержимое
            UserValidator.isValidUser(newUser);
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь после обновления: {}", newUser);
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    //получение пользователя по айди
    @Override
    public User findUserById(Long userId) {
        return users.values().stream()
                .filter(u -> u.getId()==(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    //возвращает true, если пользователь уже находится в списке друзей другого пользователя
    @Override
    public boolean isFriend(long userId, long friendId) {
        return users.get(userId).getFriends().contains(friendId);
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}