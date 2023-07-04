package de.b3nk4n.gamecloud.catalogservice.model;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record Game(
        @NotBlank(message = "Game ID must be defined.")
        String id,
        @NotBlank(message = "Game title must be defined.")
        String title,
        @NotNull(message = "Game genre must be defined.")
        GameGenre genre,
        @Nullable
        String publisher,
        @PositiveOrZero(message = "Game price must be positive or free.")
        double price
) {
}
