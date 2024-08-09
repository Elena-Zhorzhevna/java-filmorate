package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dto.mapperDto.FilmDtoMapper;
import ru.yandex.practicum.filmorate.storage.dto.modelDto.FilmDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс, который обрабатывает операции и взаимодействия, связанные с фильмами.
 * Во всех случаях возвращает объекты FilmDto.
 */
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDbStorage likeDbStorage;
    private final RatingMpaDbStorage ratingMpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeDbStorage likeDbStorage,
                       RatingMpaDbStorage ratingMpaDbStorage, GenreDbStorage genreDbStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
        this.ratingMpaDbStorage = ratingMpaDbStorage;
        this.genreDbStorage = genreDbStorage;
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
        Film film = this.fillFullData(filmStorage.findFilmById(id));
        return FilmDtoMapper.mapToFilmDto(film);
    }

    /**
     * Добавление фильма.
     *
     * @param film Добавляемый фильм.
     * @return Добавленный фильм.
     */
    public FilmDto createFilm(Film film) {
        long filmById = filmStorage.create(film).getId();
        film.getGenres().forEach(genre -> genreDbStorage.addGenre(filmById, genre.getId()));
        return this.getFilmById(filmById);
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
        log.info("Пользователь {} поставил лайк фильму {}", user.getName(), film.getName());
        return this.getFilmById(filmId);
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
        if (film == null) throw new NotFoundException("Не существует Фильма с таким id=" + filmId);
        if (user == null) throw new NotFoundException("Не существует Пользователя с таким id=" + userId);
        likeDbStorage.deleteLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк, поставленный фильму " + film.getName());
        return this.getFilmById(filmId);
    }

    /**
     * Получение списка самых популярных фильмов.
     *
     * @param filmsCount Количество возвращаемых популярных фильмов, по умолчанию - 10.
     * @return Список самых популярных фильмов по количеству лайков.
     */
    public List<FilmDto> getTopFilms(Integer filmsCount) {
        Collection<Film> films = filmStorage.findAll();
        log.info("Список самых популярных фильмов:");
        return films.stream()
                .filter(film -> film.getLikes() != null)
                .map(this::fillFullData)
                .map(FilmDtoMapper::mapToFilmDto)
                .sorted(byLikesCountBiggerFirst())
                .limit(filmsCount)
                .collect(Collectors.toList());
    }

    /**
     * Метод, сравнивающий два фильма по количеству лайков.
     */
    private Comparator<FilmDto> byLikesCountBiggerFirst() {
        return Comparator.comparingInt((FilmDto filmDto) -> filmDto.getLikes().size()).reversed();
    }

    /**
     * Метод,заполняющий необходимые поля у фильма.
     *
     * @param film Требуемый фильм.
     * @return Фильм с заполненными полями.
     */
    private Film fillFullData(Film film) {
        long filmId = film.getId();
        if (film.getMpa() != null) {
            film.setMpa(ratingMpaDbStorage.getRatingMpa(film.getMpa().getId()));
        }
        film.setGenres(new HashSet<>(genreDbStorage.getFilmGenres(filmId)));
        film.setLikes(new HashSet<>(likeDbStorage.getLikes(filmId)));
        return film;
    }
}