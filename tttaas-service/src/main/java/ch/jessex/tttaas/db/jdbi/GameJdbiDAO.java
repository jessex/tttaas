package ch.jessex.tttaas.db.jdbi;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import ch.jessex.tttaas.api.v1.Board;
import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.api.v1.Player;
import ch.jessex.tttaas.api.v1.State;
import ch.jessex.tttaas.db.GameDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Transaction;
import org.skife.jdbi.v2.TransactionStatus;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The standard JDBI-based implementation of the {@link ch.jessex.tttaas.db.GameDAO}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class GameJdbiDAO implements GameDAO {

    private final TttaasJdbiSqlObject sqlObject;
    private final ObjectMapper objectMapper;

    public GameJdbiDAO(DBI dbi, ObjectMapper objectMapper) {
        this.sqlObject = checkNotNull(dbi, "dbi cannot be null").onDemand(TttaasJdbiSqlObject.class);
        this.objectMapper = checkNotNull(objectMapper, "objectMapper cannot be null");
    }

    @Override
    public Optional<Game> findById(long id) {
        Iterator<GameMoveJoinRow> rows = this.sqlObject.findGameById(id);

        if (!rows.hasNext()) {
            return Optional.absent();
        }

        Game game = null;
        List<Move> moves = ImmutableList.of();
        while (rows.hasNext()) {
            GameMoveJoinRow row = rows.next();

            String boardJson = row.getBoard();
            Board board;
            try {
                board = this.objectMapper.readValue(new StringReader(boardJson), Board.class);
            }
            catch (Exception ex) {
                throw new RuntimeException(String.format("Could not parse json-ified board string [%s] " +
                        "from game.board field in database", boardJson), ex);
            }

            game = new Game(row.getGameId(), State.valueOf(row.getState()), board, moves);

            if (row.getMoveId() != 0 && row.getPlayer() != null) {
                Move move = new Move(game.getId(), Player.valueOf(row.getPlayer()), row.getX(), row.getY());
                moves = ImmutableList.<Move>builder().addAll(game.getMoves()).add(move).build();
                game = new Game(game.getId(), game.getState(), game.getBoard(), moves);
            }
        }

        return Optional.fromNullable(game);
    }

    @Override
    public Game upsert(final Game game) {
        final String boardJson;
        try {
            boardJson = this.objectMapper.writeValueAsString(game.getBoard());
        }
        catch (Exception ex) {
            throw new RuntimeException(String.format("Could not serialize game board into json: [%s]", game.getBoard()), ex);
        }

        Optional<Game> existingGame = findById(game.getId());

        if (existingGame.isPresent()) {
            return this.sqlObject.inTransaction(new Transaction<Game, TttaasJdbiSqlObject>() {
                @Override
                public Game inTransaction(TttaasJdbiSqlObject sqlObject, TransactionStatus status) throws Exception {
                    sqlObject.updateGame(game.getId(), game.getState().name(), boardJson);

                    Move latest = Iterables.getLast(game.getMoves());
                    sqlObject.insertMove(game.getId(), latest.getPlayer(), latest.getX(), latest.getY());

                    return game;
                }
            });
        }
        else {
            return this.sqlObject.inTransaction(new Transaction<Game, TttaasJdbiSqlObject>() {
                @Override
                public Game inTransaction(TttaasJdbiSqlObject sqlObject, TransactionStatus status) throws Exception {
                    long gameId = sqlObject.insertGame(game.getState().name(), boardJson);

                    return new Game(gameId, game.getState(), game.getBoard(), game.getMoves());
                }
            });
        }
    }
}
