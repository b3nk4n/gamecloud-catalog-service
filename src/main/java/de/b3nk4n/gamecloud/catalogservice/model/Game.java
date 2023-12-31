package de.b3nk4n.gamecloud.catalogservice.model;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.*;

import java.util.Date;

public record Game(
        /*
         * The technical ID (surrogate key) for persistence domain.
         */
        @Id
        Long id,
        /*
         * A game identifier that is unique per game.
         */
        @NotBlank(message = "Game ID must be defined.")
        String gameId,
        @NotBlank(message = "Game title must be defined.")
        String title,
        @NotNull(message = "Game genre must be defined.")
        GameGenre genre,
        @Nullable
        String publisher,
        @PositiveOrZero(message = "Game price must be positive or free.")
        double price,
        @CreatedDate
        Date created,
        @LastModifiedDate
        Date lastModified,
        @CreatedBy
        String creator,
        @LastModifiedBy
        String lastModifier,
        @Version
        int version
) {
        public static Game of(String gameId, String title, GameGenre genre, String publisher, double price) {
                return new Game(null, gameId, title, genre, publisher, price, null, null, null, null, 0);
        }
}
