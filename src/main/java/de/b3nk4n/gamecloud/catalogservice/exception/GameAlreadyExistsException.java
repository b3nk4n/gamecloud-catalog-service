package de.b3nk4n.gamecloud.catalogservice.exception;

public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String id) {
        super("Game with ID=" + id + " already exists.");
    }
}
