package de.b3nk4n.gamecloud.catalogservice.service;

import de.b3nk4n.gamecloud.catalogservice.exception.GameAlreadyExistsException;
import de.b3nk4n.gamecloud.catalogservice.exception.GameNotFoundException;
import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public Iterable<Game> getGames() {
        return repository.findAll();
    }

    public Game getGame(String gameId) {
        return repository.findByGameId(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
    }

    public Game addGame(Game game) {
        if (repository.existsByGameId(game.gameId())) {
            throw new GameAlreadyExistsException(game.gameId());
        }
        return repository.save(game);
    }

    public Game upsertGame(String gameId, Game game) {
        return repository.findByGameId(gameId)
                .map(prevGame -> repository.save(
                        new Game(
                                prevGame.id(),
                                prevGame.gameId(),
                                game.title(),
                                game.genre(),
                                game.publisher(),
                                game.price(),
                                prevGame.created(),
                                prevGame.lastModified(), // will be updated automatically
                                prevGame.version() // will be incremented automatically
                        )))
                .orElseGet(() -> addGame(game));
    }

    public void removeGame(String gameId) {
        if (!repository.existsByGameId(gameId)) {
            throw new GameNotFoundException(gameId);
        }
        repository.deleteByGameId(gameId);
    }
}
