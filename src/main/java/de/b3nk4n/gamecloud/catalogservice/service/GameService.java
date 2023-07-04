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

    public Game getGame(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    public Game addGame(Game game) {
        if (repository.existsById(game.id())) {
            throw new GameAlreadyExistsException(game.id());
        }
        return repository.save(game);
    }

    public Game upsertGame(String id, Game game) {
        return repository.findById(id)
                .map(prevGame -> repository.save(
                        new Game(prevGame.id(), game.title(), game.genre(), game.publisher(), game.price())))
                .orElseGet(() -> addGame(game));
    }

    public void removeGame(String id) {
        if (!repository.existsById(id)) {
            throw new GameNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
