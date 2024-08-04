package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Представляет пользователей в приложении Filmorate.
 */

@Table(name = "users")
@Data
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
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
    private Set<Friend> friends = new HashSet<>();

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

    public User(int id, String login, String email, String name, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }

    /**
     * Метод, добавляющий идентификатор пользователя в свойство friends.
     */
    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    /**
     * Метод, удаляющий индентификатор пользователя из свойства friends.
     */
    public void deleteFriend(Friend friend) {
        friends.remove(friend.getFriendId());
    }
}