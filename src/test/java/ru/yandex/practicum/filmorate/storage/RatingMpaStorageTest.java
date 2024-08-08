package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса RatingMpaStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaStorageTest {
    private final RatingMpaDbStorage mpaDbStorage;

    /**
     * Тест получения рейтинга по идентификатору.
     */
    @Test
    public void getRatingMpaByIdTest() {
        assertThat(mpaDbStorage.getRatingMpa(3).getName()).isEqualTo("PG-13");
    }

    /**
     * Тест получения всех рейтингов.
     */
    @Test
    public void getAllRatingsMpaTest() {
        assertThat(mpaDbStorage.getAllRatings().size()).isEqualTo(5);
    }
}