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
import ru.yandex.practicum.filmorate.storage.db_storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса FriendStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendStorageTest {
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;
    private final JdbcTemplate jdbc;
    private static final String DELETE_FRIENDS_QUERY = "DELETE FROM friends";
    User testUser = new User();
    User testUser2 = new User();

    @BeforeEach
    public void beforeEach() {
        testUser.setLogin("TestLogin");
        testUser.setName("TestUserName");
        testUser.setBirthday(LocalDate.parse("1999-02-02"));
        testUser.setEmail("test@email");

        testUser2.setLogin("TestLogin2");
        testUser2.setName("TestUserName2");
        testUser2.setBirthday(LocalDate.parse("1988-03-03"));
        testUser2.setEmail("test2@email");

        userStorage.create(testUser);
        userStorage.create(testUser2);
    }

    @AfterEach
    public void afterEach() {
        jdbc.update(DELETE_FRIENDS_QUERY);
    }

    /**
     * Тест добавления и получения друга.
     */
    @Test
    public void createAndGetFriendTest() {
        friendStorage.addFriend(testUser.getId(), testUser2.getId());
        assertThat(friendStorage.getFriends(testUser.getId())
                .stream()
                .toList()
                .getFirst()
                .getId())
                .isEqualTo(testUser2.getId());
    }

    /**
     * Тест удаления друга.
     */
    @Test
    public void deleteFriendTest() {
        friendStorage.addFriend(testUser.getId(), testUser2.getId());
        assertThat(friendStorage.getFriends(testUser.getId())
                .stream()
                .toList()
                .getFirst()
                .getId())
                .isEqualTo(testUser2.getId());
        friendStorage.deleteFriend(testUser.getId(), testUser2.getId());
        assertThat(friendStorage.getFriends(testUser.getId()).size()).isEqualTo(0);
    }
}