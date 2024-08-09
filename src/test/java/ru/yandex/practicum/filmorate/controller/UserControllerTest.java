package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.UserDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final UserService userService;
    User controllerTestUser = new User();
    User controllerTestUser2 = new User();

    @BeforeEach
    public void beforeEach() {
        controllerTestUser.setLogin("ControllerTestLogin");
        controllerTestUser.setName("ControllerTestName");
        controllerTestUser.setBirthday(LocalDate.parse("2020-12-12"));
        controllerTestUser.setEmail("controllerTest@email");

        controllerTestUser2.setLogin("ControllerTestLogin2");
        controllerTestUser2.setName("ControllerTestName2");
        controllerTestUser2.setBirthday(LocalDate.parse("1990-09-09"));
        controllerTestUser2.setEmail("controllerTest2@email");
    }

    @AfterEach
    void afterEach() {
        userService.removeAllUsers();
    }

    /**
     * Тест получения всех пользователей.
     */
    @Test
    void findAllUsersTest() {
        userService.addUser(controllerTestUser);
        userService.addUser(controllerTestUser2);
        assertThat(userService.getAllUsers().size()).isEqualTo(2);
        assertThat(userService.getAllUsers().stream().map(UserDto::getLogin).toList())
                .isEqualTo(List.of("ControllerTestLogin", "ControllerTestLogin2"));
    }

    /**
     * Тест создания пользователя.
     */
    @Test
    void userCreationTest() {
        User testCreationUser = new User("TestCreationUserLogin", "test@email", "TestUserName",
                LocalDate.parse("2020-10-10"));
        userService.addUser(testCreationUser);
        assertThat(userService.getAllUsers().size()).isEqualTo(1);
        assertThat(userService.getUserById(testCreationUser.getId()).get().getLogin())
                .isEqualTo("TestCreationUserLogin");
        assertThat(userService.getUserById(testCreationUser.getId()).get().getEmail())
                .isEqualTo("test@email");
    }

    /**
     * Тест обновления существующего пользователя.
     */
    @Test
    void updateExistingUserTest() {
        UserDto existingUser = userService.addUser(controllerTestUser2);
        User newUser = new User(existingUser.getId(), "TestUserToUpdateLogin",
                "TestUser@email.ru", LocalDate.parse("1999-09-09"));
        try {
            UserDto updatedUser = userService.updateUser(newUser);
            assertThat(updatedUser.getLogin()).isEqualTo("TestUserToUpdateLogin");
            assertThat(updatedUser.getEmail()).isEqualTo("TestUser@email.ru");
            assertThat(updatedUser.getBirthday()).isEqualTo("1999-09-09");
        } catch (NotFoundException e) {
            fail("NotFoundException was thrown");
        }
    }

    /**
     * Тест обновления несуществующего пользователя.
     */
    @Test
    void nonExistingUserUpdateTest() {
        User nonExistingUser = new User(controllerTestUser.getId(), "login", "test@email.ru",
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
        assertThat(expectedName).isEqualTo("testLogin");
    }
}