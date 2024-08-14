package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования методов класса GenreStorage.
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    private final GenreDbStorage genreDbStorage;

    /**
     * Тест получения жанра по айди.
     */
    @Test
    public void getGenreByIdTest() {
        assertThat(genreDbStorage.findGenreById(4).getName()).isEqualTo("Триллер");
    }

    /**
     * Тест получения всех лайков.
     */
    @Test
    public void getAllGenresTest() {
        assertThat(genreDbStorage.getAllGenres().size()).isEqualTo(6);
    }
}