package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.exception.GameAlreadyExistsException;
import de.b3nk4n.gamecloud.catalogservice.exception.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
public final class GameCatalogControllerAdvice {
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(GameNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(GameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String alreadyExistsHandler(GameAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> validationErrorHandler(MethodArgumentNotValidException exception) {
        return exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
