package de.b3nk4n.gamecloud.catalogservice.demo;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import de.b3nk4n.gamecloud.catalogservice.repository.GameRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
public class DemoDataLoader {
    private final GameRepository gameRepository;

    public DemoDataLoader(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDemoGames() {
        gameRepository.save(
                new Game("1234", "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99));
        gameRepository.save(
                new Game("2345", "Pacman", GameGenre.ARCADE, "Atari", 3.95));
        gameRepository.save(
                new Game("3456", "Counterstrike", GameGenre.FPS, "Valve", 19.90));
    }
}
