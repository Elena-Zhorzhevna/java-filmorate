package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    UserService userService;
    Map<Long, User> users = new HashMap<>();

    @AfterEach
    void afterEach() {
        users.clear();
        userService.removeAllUsers();
    }

    /**
     * Тест получения всех пользователей.
     */
    @Test
    void findAllUsersTest() {
        User user1 = new User(1, "Jack", "flower@m.ru",
                LocalDate.of(2000, Month.NOVEMBER, 25));
        users.put(user1.getId(), user1);
        userService.addUser(user1);
        User user2 = new User(2, "Helen", "millenium@m.ru",
                LocalDate.of(1998, Month.APRIL, 20));
        users.put(user2.getId(), user2);
        userService.addUser(user2);
        String expected = "[" + user1.toString() + ", " + user2.toString() + "]";
        // Проверка, что метод findAll() возвращает всех пользователей из коллекции
        String result = userService.getAllUsers().toString();
        assertEquals(expected, result);
    }

    /**
     * Тест создания пользователя.
     */
    @Test
    void userCreationTest() {
        String email = "test@example.ru";
        String login = "testLogin";
        LocalDate birthday = LocalDate.of(2000, Month.MAY, 1);
        User user = new User(email, login, birthday);
        try {
            users.put(user.getId(), user);
            assertEquals(user.getId(), users.get(user.getId()).getId());
        } catch (ValidationException e) {
            fail("ValidationException was thrown");
        }
    }

    /**
     * Тест обновления существующего пользователя.
     */
    @Test
    void updateExistingUserTest() {
        String login = "TestUserToUpdate";
        String email = "TestUser@email.ru";
        String name = "TestUserName";
        LocalDate birthday = LocalDate.of(1999, Month.APRIL, 1);
        User existingUser = new User(login, email, name, birthday);
        userService.addUser(existingUser);
        User newUser = new User(existingUser.getId(), "NewLogin", "NewUser@email.ru", birthday);
        users.put(newUser.getId(), newUser);
        try {
            User updatedUser = userService.updateUser(newUser);
            assertEquals(updatedUser.getLogin(), "NewLogin");
            assertEquals(updatedUser.getEmail(), "NewUser@email.ru");
            assertEquals(updatedUser.getBirthday(), birthday);
        } catch (NotFoundException e) {
            fail("NotFoundException was thrown");
        }
    }

    /**
     * Тест обновления несуществующего пользователя.
     */
    @Test
    void nonExistingUserUpdateTest() {
        User nonExistingUser = new User("login", "test@email.ru",
                LocalDate.of(2000, Month.NOVEMBER, 10));
        final Exception actualException = Assertions.assertThrows(NotFoundException.class,
                () -> {
                    userService.updateUser(nonExistingUser);
                });
    }

    /**
     * Тест создания пользователя с датой рождения в будущем.
     */
    @Test
    public void futureBirthdayTest() {
        String expectedExceptionMessage = "Дата рождения не может быть в будущем.";
        LocalDate futureDate = LocalDate.now().plusDays(1);
        User user = new User("test@email.ru", "testUser", LocalDate.of(1998, Month.MAY, 10));
        user.setBirthday(futureDate);
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    userService.addUser(user);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Тест создания пользователя с недопустимым логином.
     */
    @Test
    public void loginWithSpacesTest() {
        String expectedExceptionMessage = "Логин пустой или содержит пробелы.";
        User user = new User("test@email.ru", null, LocalDate.of(1998, Month.MAY, 10));
        user.setLogin("login with spaces");
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    userService.addUser(user);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Тест создания пользователя с недопустимым форматом электронной почты.
     */
    @Test
    public void invalidEmailTest() {
        String expectedExceptionMessage = "Имейл не указан или введен некорректно.";
        User user = new User("testUser", null, LocalDate.of(1998, Month.MAY, 10));
        user.setEmail("wrongEmailru");
        final Exception actualException = Assertions.assertThrows(ValidationException.class,
                () -> {
                    userService.addUser(user);
                });
        Assertions.assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    /**
     * Тест, что имя для отображения может быть пустым — в таком случае будет использован логин.
     */
    @Test
    public void nameCanBeEmpty() {
        User testUser = new User("test@email.ru", "testLogin", LocalDate.parse("1995-11-12"));
        userService.addUser(testUser);
        String expectedName = userService.getAllUsers()
                .stream()
                .findFirst()
                .get()
                .getName();
        assertEquals(expectedName, "testLogin");
    }
}