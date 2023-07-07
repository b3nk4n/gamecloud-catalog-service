package de.b3nk4n.gamecloud.catalogservice.repository;

import de.b3nk4n.gamecloud.catalogservice.model.Game;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByGameId(String gameId);
    boolean existsByGameId(String gameId);

    @Modifying
    @Transactional
    @Query("DELETE FROM game WHERE game_id = :gameId")
    void deleteByGameId(String gameId);
}
