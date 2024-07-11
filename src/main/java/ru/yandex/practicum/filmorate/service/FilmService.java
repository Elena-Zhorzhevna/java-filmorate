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

    //получение всех фильмов
    public Collection<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    //добавление фильма
    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    //обновление фильма
    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    //получение фильма по айди
    public Film getFilmById(long id) {
        return filmStorage.findFilmById(id);
    }

    //метод добавляет лайк фильму
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

    //метод удаляет лайк, поставленный фильму
    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        film.deleteLike(userId);
        log.info("Пользователь " + user.getName() + " удалил лайк, поставленный фильму " + film.getName());
        return film;
    }

    public List<Film> getTopFilms(Integer filmsCount) {
        Collection<Film> films = filmStorage.findAll();
        log.info("Список десяти самых популярных фильмов:" );
        return films.stream()
                .sorted(this::compare)
                .limit(filmsCount)
                .collect(Collectors.toList());
    }

    private int compare(Film film1, Film film2) {
        return -1 * Integer.compare(film1.getLikes().size(), film2.getLikes().size());
    }
}