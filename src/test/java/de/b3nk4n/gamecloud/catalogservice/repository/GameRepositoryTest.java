package de.b3nk4n.gamecloud.catalogservice.repository;

import de.b3nk4n.gamecloud.catalogservice.config.GameDataConfig;
import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest // each test runs in a transaction and changes are rolled back to keep the database clean
@Import(GameDataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integrationtest")
@Tag("IntegrationTest")
class GameRepositoryTest {
    private static final Game testGame = Game.of(UUID.randomUUID().toString(), "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private JdbcAggregateTemplate jdbcDb;

    @Test
    void findGameByGameIdWhenExisting() {
        var createdGame = jdbcDb.insert(testGame);

        var actual = gameRepository.findByGameId(createdGame.gameId());

        assertThat(actual).isPresent();
        assertThat(actual.get().gameId()).isEqualTo(testGame.gameId());
    }

    @Test
    void findGameByIdWhenExisting() {
        var createdGame = jdbcDb.insert(testGame);

        var actual = gameRepository.findById(createdGame.id());

        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(createdGame.id());
    }
}
