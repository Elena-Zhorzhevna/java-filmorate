package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db_storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса UserStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {

    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbc;
    private static final String DELETE_USERS_QUERY = "DELETE FROM users";
    User testUser = new User();

    @BeforeEach
    public void beforeEach() {
        testUser.setLogin("TestLogin");
        testUser.setName("TestName");
        testUser.setBirthday(LocalDate.parse("1999-11-11"));
        testUser.setEmail("test@email");
    }

    @AfterEach
    public void afterEach() {
        jdbc.update(DELETE_USERS_QUERY);
    }

    /**
     * Тест создания пользователя.
     */
    @Test
    public void createUserTest() {
        userStorage.create(testUser);
        assertThat(userStorage.findAll().size()).isEqualTo(1);
        assertThat(userStorage.findUserById(testUser.getId()).getName()).isEqualTo("TestName");
    }

    /**
     * Тест получения пользователя по идентификатору.
     */
    @Test
    public void findUserByIdTest() {
        userStorage.create(testUser);
        User newUser = userStorage.findUserById(testUser.getId());
        assertThat(newUser.getLogin()).isEqualTo("TestLogin");
        assertThat(newUser.getName()).isEqualTo("TestName");
        assertThat(newUser.getBirthday()).isEqualTo(LocalDate.parse("1999-11-11"));
        assertThat(newUser.getEmail()).isEqualTo("test@email");
    }

    /**
     * Тест обновления пользователя.
     */
    @Test
    public void testUpdateUser() {
        userStorage.create(testUser);
        testUser.setName("NewTestName");
        testUser.setLogin("NewTestLogin");
        testUser.setBirthday(LocalDate.parse("1988-12-12"));
        userStorage.update(testUser);
        assertThat(userStorage.findUserById(testUser.getId()).getName()).isEqualTo("NewTestName");
        assertThat(userStorage.findUserById(testUser.getId()).getLogin()).isEqualTo("NewTestLogin");
        assertThat(userStorage.findUserById(testUser.getId()).getBirthday())
                .isEqualTo(LocalDate.parse("1988-12-12"));
    }

    /**
     * Тест получения всех пользователей.
     */
    @Test
    public void getAllUsers() {
        userStorage.create(testUser);
        userStorage.create(new User("TestLogin2", "Test@email2", "TestUser2",
                LocalDate.parse("1987-10-10")));
        assertThat(userStorage.findAll().size()).isEqualTo(2);
        assertThat(userStorage.findAll().stream().map(User::getLogin).toList())
                .isEqualTo(List.of("TestLogin", "TestLogin2"));
    }

    /**
     * Тест удаления всех пользователей.
     */
    @Test
    void removeAllUsersTest() {
        userStorage.create(testUser);
        userStorage.create(new User("new@email", "newLogin", LocalDate.parse("1999-03-03")));
        assertThat(userStorage.findAll().size()).isEqualTo(2);
        userStorage.removeAllUsers();
        assertThat(userStorage.findAll().size()).isEqualTo(0);
    }
}