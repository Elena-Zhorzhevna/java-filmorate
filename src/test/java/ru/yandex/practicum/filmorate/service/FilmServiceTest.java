/*
package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
 //   FilmService filmService = new FilmService(userSr);
   */
/* UserStorage userStorage; //= new InMemoryUserStorage();
    FilmStorage filmStorage; // = new InMemoryFilmStorage();*//*


    public FilmServiceTest(FilmService filmService) {
        this.filmService = filmService;
      */
/*  this.userStorage = userStorage;
        this.filmStorage = filmStorage;*//*

    }
    //= new FilmService(userStorage, filmStorage);

    Map<Long, Film> films = new HashMap<>();

    @AfterEach
    void afterEach() {
        films.clear();
    }

    @Test
        //тест получения всех фильмов
    void findAllFilmsTest() {
        Film film1 = new Film("testFilm1", "Comedy",
                LocalDate.of(1995, Month.MAY, 12), 180);
        films.put(film1.getId(), film1);
        filmService.createFilm(film1);
        Film film2 = new Film("testFilm2", "Triller",
                LocalDate.of(1998, Month.APRIL, 20), 200);
        films.put(film2.getId(), film2);
        filmService.createFilm(film2);
        String result = "[" + film1.toString() + ", " + film2.toString() + "]";
        String expected = filmService.getAllFilms().toString();
        assertEquals(expected, result);
    }

}
*/
