package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    //получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    //добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        String filmDescription = film.getDescription();
        // проверяем выполнение необходимых условий
        //название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        //максимальная длина описания — 200 символов
        if (filmDescription.length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше создания кино.");
        }
        //продолжительность фильма должна быть положительным числом
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.debug("Фильм до обновления: {}", oldFilm);
            // если фильм найден и все условия соблюдены, обновляем его содержимое
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм после обновления: {}", newFilm);
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}