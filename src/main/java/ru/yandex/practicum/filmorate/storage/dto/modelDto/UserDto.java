package ru.yandex.practicum.filmorate.storage.dto.modelDto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Friend;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс представляет объект, который будет возвращать сервис при запросе данных о пользователе.
 */
@Data
public class UserDto {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Friend> friends = new HashSet<>();

}