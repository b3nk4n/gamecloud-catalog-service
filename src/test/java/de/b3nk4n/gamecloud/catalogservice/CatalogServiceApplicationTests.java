package de.b3nk4n.gamecloud.catalogservice;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("IntegrationTest")
class CatalogServiceApplicationTests {

    @Autowired
    WebTestClient webClient;

    @Test
    void whenPostRequestThenGameCreated() {
        var expectedGame = new Game("1234", "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

        webClient.post()
                .uri("/games")
                .bodyValue(expectedGame)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Game.class).value(actualGame -> {
                    assertThat(actualGame).isEqualTo(expectedGame);
                });
    }

}
