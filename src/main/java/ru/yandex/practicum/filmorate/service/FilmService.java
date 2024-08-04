package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.FilmDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с фильмами.
 */
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDbStorage likeDbStorage;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeDbStorage likeDbStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
    }
    /**
     * Получение всех фильмов.
     *
     * @return Коллекция всех фильмов.
     */
    public Collection<FilmDto> getAllFilms() {
        log.info("Все фильмы: " + filmStorage.findAll());
        return filmStorage.findAll().stream()
                .map(FilmDtoMapper::mapToFilmDto)
                .sorted(Comparator.comparingLong(FilmDto::getId))
                .collect(Collectors.toList());
    }

    /**
     * Получение фильма по идентификатору.
     *
     * @param id Идентификатор фильма.
     * @return Фильм с указанным идентификатором.
     */
    public FilmDto getFilmById(long id) {
        return FilmDtoMapper.mapToFilmDto(filmStorage.findFilmById(id));
    }

    /**
     * Добавление фильма.
     *
     * @param film Добавляемый фильм.
     * @return Добавленный фильм.
     */
    public FilmDto createFilm(Film film) {
        return FilmDtoMapper.mapToFilmDto(filmStorage.create(film));
    }

    /**
     * Обновление существующего фильма.
     *
     * @param film Фильм с обновленной информацией.
     * @return Обновленный фильм.
     */
    public FilmDto updateFilm(Film film) {
        return FilmDtoMapper.mapToFilmDto(filmStorage.update(film));
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
    public FilmDto addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        likeDbStorage.addLike(filmId, userId);
       /* if (filmStorage.isLiked(filmId, userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил лайк фильму");
        }*/
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return FilmDtoMapper.mapToFilmDto(filmStorage.findFilmById(filmId));
    }


    /**
     * Удаление лайка, поставленного фильму.
     *
     * @param filmId Идентификатор фильма, у которого удаляется лайк.
     * @param userId Идентификатор пользователя, чей лайк удаляется.
     * @return Фильм с обновленной информацией.
     */
    public FilmDto deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        likeDbStorage.deleteLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк, поставленный фильму " + film.getName());
        return FilmDtoMapper.mapToFilmDto(filmStorage.findFilmById(filmId));
    }

    /**
     * Получение списка самых популярных фильмов.
     *
     * @param filmsCount Количество возвращаемых популярных фильмов, по умолчанию - 10.
     * @return Список самых популярных фильмов по количеству лайков.
     */


    /*public List<FilmDto> getTopFilms(long filmsCount) {
        log.info("Список самых популярных фильмов:");
        return filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .map(FilmDtoMapper::mapToFilmDto)
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(filmsCount)
                .collect(Collectors.toList());
                //.toList();
    }*/
    public List<FilmDto> getTopFilms(Integer filmsCount) {
        Collection<Film> films = filmStorage.findAll();
        log.info("Список самых популярных фильмов:");
        return films.stream()
                .filter(film -> film.getLikes() != null)
                .map(FilmDtoMapper::mapToFilmDto)
                .sorted(this::compare)
                .limit(filmsCount)
                .collect(Collectors.toList());
    }

    private int compare(FilmDto film1, FilmDto film2) {
        return -1 * Integer.compare(film1.getLikes().size(), film2.getLikes().size());
    }
}