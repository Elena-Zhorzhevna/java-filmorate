package ru.yandex.practicum.filmorate.validator;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Класс для валидации объекта User.
 */
public class UserValidator {
    /**
     * Проверяем выполнение необходимых условий.
     *
     * @param user Пользователь, для которого нужна проверка валидации.
     * @return Является ли объект удовлетворяющим требованиям.
     */
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

    /**
     * Электронная почта не может быть пустой и должна содержать символ @.
     *
     * @param email Проверяемая электронная почта.
     * @return Является ли проверяемая почта удовлетворяющей условиям.
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        } else {
            return email.contains("@");
        }
    }

    /**
     * Логин не пустой и не содержит пробелы.
     *
     * @param login Проверяемый логин.
     * @return Является ли проверяемый логин удовлетворяющим условиям.
     */
    private static boolean isValidLogin(String login) {
        return !login.isEmpty() && !login.contains(" ");
    }

    /**
     * Имя для отображения может быть пустым — в таком случае будет использован логин.
     *
     * @param name Проверяемое имя.
     * @return Является ли проверяемое имя удовлетворяющим условиям.
     */
    private static boolean isEmptyName(String name) {
        return (name == null || name.isEmpty());
    }

    /**
     * Дата рождения не может быть в будущем.
     *
     * @param birthday Проверяемая дата рождения.
     * @return Является ли проверяемая дата рождения удовлетворяющей условиям.
     */
    private static boolean isValidBirthday(LocalDate birthday) {
        return birthday.isBefore(LocalDate.now());
    }
}