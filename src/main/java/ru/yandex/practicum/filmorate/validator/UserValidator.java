package ru.yandex.practicum.filmorate.validator;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    // проверяем выполнение необходимых условий
    public static boolean isValidUser(@RequestBody User user) {

        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("Имейл не указан или введен некорректно.");
        }

        if (!isValidLogin(user.getLogin())) {
            throw new ValidationException("Логин пустой или содержит пробелы.");
        }

        if (isEmptyName(user.getName())) {
            user.setName(user.getLogin());
        }

        if (!isValidBirthday(user.getBirthday())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        return true;
    }

    //электронная почта не может быть пустой и должна содержать символ @
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        } else {
            return email.contains("@");
        }
    }

    //логин не пустой и не содержит пробелы
    private static boolean isValidLogin(String login) {
        return !login.isEmpty() && !login.contains(" ");
    }

    //имя для отображения может быть пустым — в таком случае будет использован логин
    private static boolean isEmptyName(String name) {
        return (name == null || name.isEmpty());
    }

    //дата рождения не может быть в будущем
    private static boolean isValidBirthday(LocalDate birthday) {
        return birthday.isBefore(LocalDate.now());
    }
}