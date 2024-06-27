package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    //получение всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    //добавление пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        // проверяем выполнение необходимых условий
        //электронная почта не может быть пустой и должна содержать символ @
        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("Имейл не указан или введен некорректно.");
        }
        //логин не пустой и не содержит пробелы
        if (!isValidLogin(user.getLogin())) {
            throw new ValidationException("Логин пустой или содержит пробелы.");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        //дата рождения не может быть в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.debug("Пользователь до обновления: {}", oldUser);
            // если пользователь найден и все условия соблюдены, обновляем её содержимое
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь после обновления: {}", newUser);
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public static boolean isValidEmail(String email) {
        return !email.isEmpty() && email.contains("@");
    }

    public static boolean isValidLogin(String login) {
        return !login.isEmpty() && !login.contains(" ");
    }
}