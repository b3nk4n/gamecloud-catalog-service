package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.service.GameService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("games")
public final class GameCatalogController {
    private static final Logger log = LoggerFactory.getLogger(GameCatalogController.class);

    private final GameService gameService;

    public GameCatalogController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Iterable<Game> get() {
        return gameService.getGames();
    }

    @GetMapping("{gameId}")
    public Game getById(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game post(@Valid @RequestBody Game game, Authentication principal) {
        log.info("POST received of principal with authorities: {}", principal.getAuthorities());

        return gameService.addGame(game);
    }

    @PutMapping("{gameId}")
    public Game put(@PathVariable String gameId, @Valid @RequestBody Game game) {
        return gameService.upsertGame(gameId, game);
    }

    @DeleteMapping("{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String gameId) {
        gameService.removeGame(gameId);
    }
}
