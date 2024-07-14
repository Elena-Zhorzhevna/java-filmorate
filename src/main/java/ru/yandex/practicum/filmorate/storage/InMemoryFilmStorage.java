package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Имплементирует интерфейс FilmStorage, содержит логику хранения, обновления и поиска объектов Film.
 */
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Long, Film> films = new HashMap<>();

    /**
     * Получение всех фильмов.
     *
     * @return Коллекция всех фильмов.
     */
    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    /**
     * Добавление фильма.
     *
     * @param film Добавляемый фильм.
     * @return Добавленный фильм.
     */
    @Override
    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        FilmValidator.isValidFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    /**
     * Обновление существующего фильма.
     *
     * @param newFilm Фильм с обновленной информацией.
     * @return Обновленный фильм.
     */
    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.debug("Фильм до обновления: {}", oldFilm);
            // если фильм найден и все условия соблюдены, обновляем его содержимое
            FilmValidator.isValidFilm(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм после обновления: {}", newFilm);
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    /**
     * Получение фильма по идентификатору.
     *
     * @param filmId Идентификатор фильма.
     * @return Фильм с указанным идентификатором.
     */
    @Override
    public Film findFilmById(Long filmId) {
        return films.values().stream()
                .filter(u -> u.getId() == (filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id %d не найден", filmId)));
    }

    /**
     * Метод, проверяющий наличие лайка пользователя у данного фильма.
     *
     * @param filmId Идентификатор фильма, у которого проверяется наличие лайка.
     * @param userId Идентификатор пользователя, чей лайк проверяется у заданного фильма.
     * @return Наличие лайка заданного пользователя у заданного фильма.
     */
    @Override
    public boolean isLiked(long filmId, long userId) {
        return films.get(filmId).getLikes().contains(userId);
    }

    /**
     * Удаление всех фильмов.
     */
    @Override
    public void removeAllFilms() {
        films.clear();
    }

    /**
     * Вспомогательный метод для генерации идентификатора нового пользователя.
     *
     * @return Сгенерированный уникальный идентификатор фильма.
     */
    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}