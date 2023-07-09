package de.b3nk4n.gamecloud.catalogservice;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Tag("IntegrationTest")
class CatalogServiceApplicationTest {

    @Autowired
    WebTestClient webClient;

    @Test
    void whenPostRequestThenGameCreated() {
        var expectedGame = Game.of(UUID.randomUUID().toString(), "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

        webClient.post()
                .uri("/games")
                .bodyValue(expectedGame)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Game.class).value(actualGame -> {
                    assertThat(actualGame)
                            .usingRecursiveComparison().ignoringFields("id", "created", "lastModified", "version")
                            .isEqualTo(expectedGame);
                    assertThat(actualGame.id()).isNotNull();
                    assertThat(actualGame.created()).isNotNull();
                    assertThat(actualGame.lastModified()).isNotNull();
                    assertThat(actualGame.version()).isNotNull();
                });
    }

}