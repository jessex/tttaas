package ch.jessex.tttaas.core;

import java.util.List;

import ch.jessex.tttaas.core.model.Board;
import ch.jessex.tttaas.core.model.Game;
import ch.jessex.tttaas.core.model.Move;
import ch.jessex.tttaas.core.model.Player;
import ch.jessex.tttaas.core.model.State;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * Provides utilities for operating a {@link Game}.
 *
 * @author jessex
 * @since 0.0.1
 */
public final class Mover {
    private static final short MAX_MOVES = 9;
    private static final short FIRST_POSSIBLE_WIN = 5;

    /**
     * Determines whether or not the given {@link Move}, when applied to the given {@link Game}, is valid.
     *
     * @param move the move to check
     * @param game the game to check the move on
     * @return an Optional containing an InvalidityReason if the move is invalid; an absent Optional otherwise
     */
    public static Optional<Move.InvalidityReason> validate(Move move, Game game) {
        if (State.ONGOING != game.getState()) {
            return Optional.of(new Move.InvalidityReason("Game [%d] already completed", game.getId()));
        }

        int x = move.getXCoordinate();
        int y = move.getYCoordinate();
        Optional<Player> optionalPlayer = game.getBoard().getPlayerAtPosition(x, y);

        if (optionalPlayer.isPresent()) {
            return Optional.of(new Move.InvalidityReason("Player %s already at coordinates (%d, %d) in game [%d]",
                    optionalPlayer.get().getLetter(), x, y, game.getId()));
        }

        return Optional.absent();
    }

    /**
     * Applies the given {@link Move} to the given {@link Game} and returns the new, transformed game. Assumes that the given
     * move has been validated beforehand.
     *
     * @param move the move to apply
     * @param game the game to apply the move to
     * @return the new game
     */
    public static Game move(Move move, Game game) {
        List<Move> moves = ImmutableList.<Move>builder().addAll(game.getMoves()).add(move).build();
        Board board = game.getBoard().withNewMove(move);
        State state = evaluate(board, moves, move);

        return new Game(game.getId(), state, board, moves);
    }

    /**
     * Returns the state of the game with the given board and the given moves applied.
     *
     * @param board the current board to evaluate
     * @param moves the list of moves performed thus far
     * @param mostRecent the most recent move
     * @return the current state of the game
     */
    private static State evaluate(Board board, List<Move> moves, Move mostRecent) {
        if (moves.size() < FIRST_POSSIBLE_WIN) {
            return State.ONGOING;
        }

        int n = 3;
        int x = mostRecent.getXCoordinate();
        int y = mostRecent.getYCoordinate();
        Player[][] squares = board.getSquares();

        Player player = mostRecent.getPlayer();
        State victory = player == Player.X ? State.X_VICTORY : State.O_VICTORY;

        // Check row
        for (int i = 0; i < n; i++) {
            if (player != squares[y][i]) {
                break;
            }
            if (i == n - 1) {
                return victory;
            }
        }

        // Check column
        for (int i = 0; i < n; i++) {
            if (player != squares[i][x]) {
                break;
            }
            if (i == n - 1) {
                return victory;
            }
        }

        // Check diagonal if on diagonal
        if (x == y) {
            for (int i = 0; i < n; i++) {
                if (player != squares[i][i]) {
                    break;
                }
                if (i == n - 1) {
                    return victory;
                }
            }
        }

        // Check anti-diagonal if on anti-diagonal
        if (x + y == n - 1) {
            for (int i = 0; i < n; i++) {
                if (player != squares[i][n - i - 1]) {
                    break;
                }
                if (i == n - 1) {
                    return victory;
                }
            }
        }

        return moves.size() == MAX_MOVES ? State.CATS_GAME : State.ONGOING;
    }
}
