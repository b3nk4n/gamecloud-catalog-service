package de.b3nk4n.gamecloud.catalogservice.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String id) {
        super("Game with ID=" + id + " not found.");
    }
}
