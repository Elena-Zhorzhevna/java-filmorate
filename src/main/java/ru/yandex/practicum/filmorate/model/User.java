package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Представляет пользователей в приложении Filmorate.
 */
@Data
public class User {
    /**
     * Идентификатор пользователя.
     */
    private long id;
    /**
     * Логин пользователя.
     */
    private String login;
    /**
     * Электронная почта пользователя.
     */
    private String email;
    /**
     * Имя для отображения пользователя.
     */
    private String name;
    /**
     * Дата рождения пользователя.
     */
    private LocalDate birthday;
    /**
     * Свойство, содержащее список идентификаторов друзей пользователя.
     */
    private Set<Long> friends = new HashSet<>();

    @JsonCreator
    public User() {
    }

    /**
     * Конструкторы для тестирования.
     */
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

    /**
     * Метод, добавляющий идентификатор пользователя в свойство friends.
     *
     * @param id Идентификатор пользователя, который добавляется в друзья.
     */
    public void addFriend(Long id) {
        friends.add(id);
    }

    /**
     * Метод, удаляющий индентификатор пользователя из свойства friends.
     *
     * @param id Идентификатор пользователя, который удаляется из друзей.
     */
    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}