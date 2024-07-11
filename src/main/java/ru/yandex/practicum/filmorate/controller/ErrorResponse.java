package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
//специальный класс с описанием ошибки, объект которого контроллер вернёт клиенту в случае возникновения проблем
public class ErrorResponse {
    // название ошибки
    String error;
    // подробное описание
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}