package de.b3nk4n.gamecloud.catalogservice.repository;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@Primary
@Repository
@ConditionalOnProperty(name = "feature-flags.in-memory-storage", havingValue = "true")
public class InMemoryGameRepository implements GameRepository {
    private static final Map<String, Game> gamesByGameId = new ConcurrentHashMap<>();

    @Override
    public <S extends Game> Iterable<S> saveAll(Iterable<S> games) {
        games.forEach(game -> gamesByGameId.put(game.gameId(), game));
        return games;
    }

    @Override
    public Optional<Game> findById(Long id) {
        return gamesByGameId.values()
                .stream()
                .filter(game -> game.id().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return gamesByGameId.values()
                .stream()
                .anyMatch(game -> game.id().equals(id));
    }

    @Override
    public Iterable<Game> findAll() {
        return gamesByGameId.values();
    }

    @Override
    public Iterable<Game> findAllById(Iterable<Long> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public long count() {
        return gamesByGameId.size();
    }

    @Override
    public void deleteById(Long id) {
        findById(id)
                .ifPresent(game -> gamesByGameId.remove(game.gameId()));
    }

    @Override
    public void delete(Game game) {
        deleteByGameId(game.gameId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Game> games) {
        games.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        gamesByGameId.clear();
    }

    @Override
    public Optional<Game> findByGameId(String gameId) {
        return Optional.ofNullable(gamesByGameId.get(gameId));
    }

    @Override
    public boolean existsByGameId(String gameId) {
        return gamesByGameId.get(gameId) != null;
    }

    @Override
    public <S extends Game> S save(S game) {
        gamesByGameId.put(game.gameId(), game);
        return game;
    }

    @Override
    public void deleteByGameId(String gameId) {
        gamesByGameId.remove(gameId);
    }
}
