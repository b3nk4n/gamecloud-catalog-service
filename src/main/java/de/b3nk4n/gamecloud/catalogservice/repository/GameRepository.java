package de.b3nk4n.gamecloud.catalogservice.repository;

import de.b3nk4n.gamecloud.catalogservice.model.Game;

import java.util.Optional;

public interface GameRepository {
    Iterable<Game> findAll();
    Optional<Game> findById(String id);
    boolean existsById(String id);
    Game save(Game game);
    void deleteById(String id);
}
