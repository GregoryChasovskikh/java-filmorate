package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {

    private final HttpStatus status;

    public NotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}