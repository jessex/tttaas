package ch.jessex.tttaas.db.jdbi;

import java.util.Iterator;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * An interface for speaking with Jdbi transactionally.
 *
 * @author jessex
 * @since 0.0.1
 */
public interface TttaasJdbiSqlObject extends Transactional<TttaasJdbiSqlObject> {

    /**
     * Returns an {@link Iterator} over the rows formed by joining some game row over zero to many move rows.
     *
     * @param gameId the id of the game to retrieve
     * @return the rows from the join query; empty if there is no game with the given id, never null
     */
    @SqlQuery("select g.id as gameId, g.state as state, g.board as board, " +
              "m.id as moveId, m.player as player, m.x as x, m.y as y " +
              "    from game g left join move m on (g.id = m.game_id) where g.id = :gameId")
    @MapResultAsBean
    Iterator<GameMoveJoinRow> findGameById(@Bind("gameId") long gameId);

    /**
     * Inserts a new row into the game table with the given state and board.
     *
     * @param state the state of the new game
     * @param board the json-ified board of the new game
     * @return the id of the new game
     */
    @GetGeneratedKeys
    @SqlUpdate("insert into game (state, board) values (:state, :board)")
    long insertGame(@Bind("state") String state, @Bind("board") String board);

    /**
     * Updates a row in the game table to have the given state and board.
     *
     * @param id the id of the game to update
     * @param state the new state
     * @param board the new board
     */
    @SqlUpdate("update game set state = :state, board = :board where id = :id")
    void updateGame(@Bind("id") long id, @Bind("state") String state, @Bind("board") String board);

    /**
     * Inserts a new row into the move table with the given game id, player, and coordinates.
     *
     * @param gameId the game that this move is part of
     * @param player the player who made the move
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @SqlUpdate("insert into move (game_id, player, x, y) values (:gameId, :player, :x, :y)")
    void insertMove(@Bind("gameId") long gameId, @Bind("player") String player, @Bind("x") int x, @Bind("y") int y);
}
