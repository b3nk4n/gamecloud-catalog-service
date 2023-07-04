package de.b3nk4n.gamecloud.catalogservice.service;

import de.b3nk4n.gamecloud.catalogservice.config.RecommendationConfig;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    private final RecommendationConfig config;

    @Value("${recommendation.game-genre}")
    private GameGenre recommendedGameGenre;

    public RecommendationService(RecommendationConfig config) {
        this.config = config;
    }

    public String getRecommendedGameTitle() {
        return config.getGameTitle();
    }

    public GameGenre getRecommendedGameGenre() {
        return recommendedGameGenre;
    }
}
