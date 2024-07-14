package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с фильмами.
 */
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    /**
     * Получение всех фильмов.
     *
     * @return Коллекция всех фильмов.
     */
    public Collection<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    /**
     * Получение фильма по идентификатору.
     *
     * @param id Идентификатор фильма.
     * @return Фильм с указанным идентификатором.
     */
    public Film getFilmById(long id) {
        return filmStorage.findFilmById(id);
    }

    /**
     * Добавление фильма.
     *
     * @param film Добавляемый фильм.
     * @return Добавленный фильм.
     */
    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    /**
     * Обновление существующего фильма.
     *
     * @param film Фильм с обновленной информацией.
     * @return Обновленный фильм.
     */
    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    /**
     * Удаление всех фильмов.
     */
    public void removeAllFilms() {
        filmStorage.removeAllFilms();
    }

    /**
     * Добавление лайка фильму.
     *
     * @param filmId Идентификатор фильма, которому ставится лайк.
     * @param userId Идентификатор пользователя, который ставит лайк.
     * @return Фильм с обновленными данными.
     */
    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        if (filmStorage.isLiked(filmId, userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил лайк фильму");
        }
        film.addLike(userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    /**
     * Удаление лайка, поставленного фильму.
     *
     * @param filmId Идентификатор фильма, у которого удаляется лайк.
     * @param userId Идентификатор пользователя, чей лайк удаляется.
     * @return Фильм с обновленной информацией.
     */
    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        film.deleteLike(userId);
        log.info("Пользователь " + user.getName() + " удалил лайк, поставленный фильму " + film.getName());
        return film;
    }

    /**
     * Получение списка самых популярных фильмов.
     *
     * @param filmsCount Количество возвращаемых популярных фильмов, по умолчанию - 10.
     * @return Список самых популярных фильмов по количеству лайков.
     */
    public List<Film> getTopFilms(Integer filmsCount) {
        Collection<Film> films = filmStorage.findAll();
        log.info("Список десяти самых популярных фильмов:");
        return films.stream()
                .sorted(this::compare)
                .limit(filmsCount)
                .collect(Collectors.toList());
    }

    /**
     * Метод, сравнивающий два фильма по количеству лайков.
     *
     * @param film1 Первый сравниваемый фильм.
     * @param film2 Второй сравниваемый фильм.
     */
    private int compare(Film film1, Film film2) {
        return -1 * Integer.compare(film1.getLikes().size(), film2.getLikes().size());
    }
}