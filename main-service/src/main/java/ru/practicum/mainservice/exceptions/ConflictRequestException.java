package ru.practicum.mainservice.exceptions;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }

    public ConflictRequestException(String message, Throwable cause) {

    }
}
