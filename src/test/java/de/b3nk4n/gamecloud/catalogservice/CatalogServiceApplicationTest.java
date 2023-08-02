package de.b3nk4n.gamecloud.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import de.b3nk4n.gamecloud.catalogservice.model.Game;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Tag("IntegrationTest")
@Testcontainers // Activates automatic startup and cleanup of test containers
class CatalogServiceApplicationTest {

    /**
     * Has role employee & customer.
     */
    private static KeycloakToken bennyToken;

    /**
     * Has only role customer.
     */
    private static KeycloakToken vanessaToken;

    @Autowired
    WebTestClient webClient;

    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("keycloak/keycloak:22.0.1")
            .withRealmImportFile("keycloak-realm-test-config.json");

    @DynamicPropertySource
    static void dynamicProps(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/GameCloud");
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "/realms/GameCloud/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        bennyToken = authenticateWith(webClient, "b3nk4n", "password");
        vanessaToken = authenticateWith(webClient, "vankwk", "password");
    }

    private static KeycloakToken authenticateWith(WebClient webClient, String username, String password) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "gamecloud-test")
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    @Test
    void whenPostRequestAuthorizedThen204() {
        var expectedGame = Game.of(UUID.randomUUID().toString(), "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

        webClient.post()
                .uri("/games")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(bennyToken.accessToken()))
                .bodyValue(expectedGame)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Game.class).value(actualGame -> {
                    assertThat(actualGame)
                            .usingRecursiveComparison()
                            .ignoringFields("id", "created", "lastModified", "creator", "lastModifier", "version")
                            .isEqualTo(expectedGame);
                    assertThat(actualGame.id()).isNotNull();
                    assertThat(actualGame.created()).isNotNull();
                    assertThat(actualGame.lastModified()).isNotNull();
                    assertThat(actualGame.version()).isNotNull();
                });
    }

    @Test
    void whenPostRequestUnauthorizedThen403() {
        var expectedGame = Game.of(UUID.randomUUID().toString(), "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

        webClient.post()
                .uri("/games")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(vanessaToken.accessToken()))
                .bodyValue(expectedGame)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        var expectedGame = Game.of(UUID.randomUUID().toString(), "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);

        webClient.post()
                .uri("/games")
                .bodyValue(expectedGame)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
