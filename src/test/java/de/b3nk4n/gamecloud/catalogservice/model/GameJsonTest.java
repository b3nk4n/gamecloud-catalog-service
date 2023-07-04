package de.b3nk4n.gamecloud.catalogservice.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GameJsonTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    JacksonTester<Game> jsonTester;

    @Test
    void serializeValidGame() throws Exception {
        var book = new Game("1234", "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99);
        var jsonContent = jsonTester.write(book);
        assertThat(jsonContent).extractingJsonPathStringValue("@.id")
                .isEqualTo(book.id());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.genre")
                .isEqualTo(book.genre().name());
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
                .isEqualTo(book.publisher());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
    }

    @Test
    void deserializeValidGame() throws Exception {
        var content = """
                {
                  "id": "1234",
                  "title": "FIFA 23",
                  "genre": "SPORTS",
                  "publisher": "EA Sports",
                  "price": 39.99
                }
                """;
        assertThat(jsonTester.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Game("1234", "FIFA 23", GameGenre.SPORTS, "EA Sports", 39.99));
    }

}
