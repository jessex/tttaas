package ch.jessex.tttaas.db;

import ch.jessex.tttaas.api.v1.Game;
import com.google.common.base.Optional;

/**
 * The DAO for accessing persisted {@link ch.jessex.tttaas.api.v1.Game} data.
 *
 * @author jessex
 * @since 0.0.1
 */
public interface GameDAO {

    /**
     * @param id the id of the game to retrieve
     * @return an Optional containing the Game if it exists; an absent Optional otherwise
     */
    Optional<Game> findById(long id);

    /**
     * Upserts the given Game and returns the persisted instance.
     *
     * @param game the Game to persist
     * @return the persisted Game instance
     */
    Game upsert(Game game);
}
