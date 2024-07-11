package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id; //идентификатор
    private String login; //логин пользователя
    private String email; //электронная почта пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения
    private Set<Long> friends = new HashSet<>(); //свойство, содержащее список id друзей пользователя

    @JsonCreator
    public User() {
    }

    //конструктор для тестирования
    public User(String login, String email, String name, LocalDate birthday) {
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String email, String login, LocalDate birthday) {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

    public User(long id, String login, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}