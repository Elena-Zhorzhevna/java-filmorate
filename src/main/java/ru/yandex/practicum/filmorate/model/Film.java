package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Data
public class Film {
    private long id; //идентификатор
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate; //дата релиза
    private int duration; //продолжительность
    //private int likesCounter; //счетчик лайков
    private Set<Long> likes = new HashSet<>(); //свойство, содержащее список id пользователей, поставивших лайк

    @JsonCreator
    public Film() {
    }

    //конструктор для тестирования
    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(Long id) {
        likes.add(id);
        //likesCounter ++;
    }

    public void deleteLike(Long id) {
        likes.remove(id);
        //likesCounter--;
    }
}