package de.b3nk4n.gamecloud.catalogservice.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void whenAllFieldCorrectThenValidationSuccess() {
        var game = new Game("1234", "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);
        var validationResults = validator.validate(game);

        assertThat(validationResults).isEmpty();
    }

    @Test
    void whenTitleIsMissingThenValidationFailure() {
        var game = new Game("1234", "", GameGenre.SPORTS, "EA Sports", 39.99);
        var validationResults = validator.validate(game);

        assertThat(validationResults).hasSize(1);
        assertThat(validationResults.iterator().next().getMessage()).contains("title must be defined");
    }
}
