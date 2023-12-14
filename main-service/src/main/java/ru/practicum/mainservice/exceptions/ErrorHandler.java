package ru.practicum.mainservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Map;


@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.mainservice")
public class ErrorHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse validationErrorHandler(final DataNotFoundException e) {
        return new ErrorResponse("Ошибка поиска", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationErrorHandler(final ConstraintViolationException e) {
        log.debug("Получен статус 400 BAD REQUEST {}", e.getMessage(), e);
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationErrorHandler(final IllegalArgumentException e) {
        log.debug("Получен статус 400 BAD REQUEST {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ConflictRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> validationErrorHandler(final ConflictRequestException e) {
        log.debug("Получен статус 409 CONFLICT {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> unknownErrorHandler(final SQLException e) {
        return Map.of("error", e.getMessage());
    }
}


