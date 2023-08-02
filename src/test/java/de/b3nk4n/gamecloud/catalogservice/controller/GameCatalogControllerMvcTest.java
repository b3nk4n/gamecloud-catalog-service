package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.config.SecurityConfig;
import de.b3nk4n.gamecloud.catalogservice.exception.GameNotFoundException;
import de.b3nk4n.gamecloud.catalogservice.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameCatalogController.class)
@Import(SecurityConfig.class)
class GameCatalogControllerMvcTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    MockMvc mockedController;

    @MockBean
    GameService mockedGameService;

    /**
     * Mock the {@link JwtDecoder} so that the application does not try to call Keycloak and get the public key
     * for decoding the Access Token.
     */
    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void whenGetGameNotExistingThenShouldReturnNotFound() throws Exception {
        String gameId = "1234";

        given(mockedGameService.getGame(gameId))
                .willThrow(GameNotFoundException.class);

        mockedController.perform(MockMvcRequestBuilders.get("/games/" + gameId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteGameWithEmployeeRoleThenShouldReturn204() throws Exception {
        final var gameId = "1234";
        mockedController.perform(delete("/games/" + gameId)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteGameWithNoEmployeeRoleThenShouldReturn403() throws Exception {
        final var gameId = "1234";
        mockedController.perform(delete("/games/" + gameId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteGameNotAuthenticatedThenShouldReturn401() throws Exception {
        final var gameId = "1234";
        mockedController.perform(delete("/games/" + gameId))
                .andExpect(status().isUnauthorized());
    }
}
