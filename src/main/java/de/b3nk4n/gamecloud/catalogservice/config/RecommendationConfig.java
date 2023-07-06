package de.b3nk4n.gamecloud.catalogservice.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.b3nk4n.gamecloud.catalogservice.model.GameGenre;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("recommendation") // listens to RefreshScopeRefreshedEvent by default via Spring Actuator
public class RecommendationConfig {
    /**
     * The owner's most favorite and generally recommended game title.
     */
    private final String gameTitle;

    /**
     * The owner's most favorite and generally recommended game genre.
     */
    private final GameGenre gameGenre;

    @JsonCreator
    public RecommendationConfig(String gameTitle, GameGenre gameGenre) {
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public GameGenre getGameGenre() {
        return gameGenre;
    }
}
