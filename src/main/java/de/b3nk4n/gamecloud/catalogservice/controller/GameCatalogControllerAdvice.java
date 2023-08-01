package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.exception.GameAlreadyExistsException;
import de.b3nk4n.gamecloud.catalogservice.exception.GameNotFoundException;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
public final class GameCatalogControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GameCatalogControllerAdvice.class);

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> validationErrorHandler(HttpMessageNotReadableException exception) {
        if (exception.getMessage().contains(GameGenre.class.getName())) {
            // Unknown or empty enum yields a rather technical error, such as the following...
            //   JSON parse error: Cannot deserialize value of type `de.b3nk4n.gamecloud.catalogservice.model.GameGenre` from String "Foo": not one of the values accepted for Enum class: [ARCADE, RTS, SIMULATION, FPS, MMORPG, RACING, SPORTS]
            //   JSON parse error: Cannot coerce empty String ("") to `de.b3nk4n.gamecloud.catalogservice.model.GameGenre` value (but could if coercion was enabled using `CoercionConfig`)
            // A more sophisticated custom annotation for validation could be created, such as described here:
            // https://www.baeldung.com/javax-validations-enums
            return Map.of("genre", "Game genre is not one of the values accepted for Enum class: [ARCADE, RTS, SIMULATION, FPS, MMORPG, RACING, SPORTS]");
        }

        logger.info(exception.getMessage());

        // Return generic error message to not leak any technical information
        return Map.of("error", "Message could not be read or contains invalid input.");
    }
}
