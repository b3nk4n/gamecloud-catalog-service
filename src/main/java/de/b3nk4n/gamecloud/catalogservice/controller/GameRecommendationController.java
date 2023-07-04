package de.b3nk4n.gamecloud.catalogservice.controller;

import de.b3nk4n.gamecloud.catalogservice.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recommendation")
public class GameRecommendationController {

    private final RecommendationService service;

    public GameRecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("title")
    public String recommendGameTitle() {
        return service.getRecommendedGameTitle();
    }

    @GetMapping("genre")
    public String recommendGameGenre() {
        return service.getRecommendedGameGenre().name();
    }
}
