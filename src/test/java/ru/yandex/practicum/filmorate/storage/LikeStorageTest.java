package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db_storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса LikeStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeStorageTest {
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbc;
    private static final String DELETE_LIKES = "DELETE FROM film_likes";
    User user1 = new User();
    User user2 = new User();
    Film film = new Film();

    @BeforeEach
    public void beforeEach() {
        user1.setLogin("TestLogin");
        user1.setName("TestName");
        user1.setBirthday(LocalDate.parse("1987-11-11"));
        user1.setEmail("test@email");

        user2.setLogin("TestLogin2");
        user2.setName("TestName2");
        user2.setBirthday(LocalDate.parse("1988-12-12"));
        user2.setEmail("test2@email");

        film.setName("TestFilmName");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.parse("1999-03-03"));
        film.setDuration(125);
        RatingMpa mpa = new RatingMpa();
        mpa.setId(1);
        film.setMpa(mpa);

        userStorage.create(user1);
        userStorage.create(user2);
        filmStorage.create(film);
    }

    @AfterEach
    public void afterEach() {
        jdbc.update(DELETE_LIKES);
    }

    /**
     * Тест добавления и получения лайка.
     */
    @Test
    public void addAndGetLikeTest() {
        likeStorage.addLike(film.getId(), user1.getId());
        likeStorage.addLike(film.getId(), user2.getId());
        assertThat(likeStorage.getLikes(film.getId()).size()).isEqualTo(2);
    }

    /**
     * Тест удаления лайка.
     */
    @Test
    public void deleteLikeTest() {
        likeStorage.addLike(film.getId(), user1.getId());
        likeStorage.addLike(film.getId(), user2.getId());
        assertThat(likeStorage.getLikes(film.getId()).size()).isEqualTo(2);
        likeStorage.deleteLike(film.getId(), user2.getId());
        assertThat(likeStorage.getLikes(film.getId()).size()).isEqualTo(1);
    }
}