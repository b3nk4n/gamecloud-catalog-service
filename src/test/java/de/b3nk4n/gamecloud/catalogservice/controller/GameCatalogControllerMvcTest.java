package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.exception.GameNotFoundException;
import de.b3nk4n.gamecloud.catalogservice.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@WebMvcTest(GameCatalogController.class)
class GameCatalogControllerMvcTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    MockMvc mockedController;

    @MockBean
    GameService mockedGameService;

    @Test
    void whenGetGameNotExistingThenShouldReturnNotFound() throws Exception {
        String gameId = "1234";

        given(mockedGameService.getGame(gameId))
                .willThrow(GameNotFoundException.class);

        mockedController.perform(MockMvcRequestBuilders.get("/games/" + gameId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
