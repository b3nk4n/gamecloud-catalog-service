package de.b3nk4n.gamecloud.catalogservice.repository;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "feature-flags.in-memory-storage", havingValue = "true")
public class InMemoryGameRepository implements GameRepository {
    private static final Map<String, Game> gamesById = new ConcurrentHashMap<>();

    @Override
    public Iterable<Game> findAll() {
        return gamesById.values();
    }

    @Override
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(gamesById.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return gamesById.get(id) != null;
    }

    @Override
    public Game save(Game game) {
        gamesById.put(game.id(), game);
        return game;
    }

    @Override
    public void deleteById(String id) {
        gamesById.remove(id);
    }
}
