package ch.jessex.tttaas.core;

import java.util.List;

import ch.jessex.tttaas.api.v1.Board;
import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.InvalidityReason;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.api.v1.Player;
import ch.jessex.tttaas.api.v1.State;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Mover}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestMover {
    private static final List<Move> MOVES = ImmutableList.of(new Move(1L, Player.X, 2, 0),
                                                             new Move(1L, Player.O, 1, 1),
                                                             new Move(1L, Player.X, 0, 0),
                                                             new Move(1L, Player.O, 1, 2));

    private static final Player[][] INCOMPLETE_SQUARES = {{Player.X, null, Player.X},
                                                          {null, Player.O, null},
                                                          {null, Player.O, null}};
    private static final Board INCOMPLETE_BOARD = new Board(INCOMPLETE_SQUARES);

    private static final Player[][] FULL_SQUARES = {{Player.O, Player.O, Player.X},
                                                    {Player.X, Player.X, Player.O},
                                                    {Player.O, Player.X, Player.X}};
    private static final Board FULL_BOARD = new Board(FULL_SQUARES);

    @Test
    public void testValidateGameComplete() {
        Game game = new Game(1L, State.CATS_GAME, FULL_BOARD, ImmutableList.<Move>of());
        Move move = new Move(1L, Player.X, 1, 1);

        Optional<InvalidityReason> optionalReason = Mover.validate(move, game);
        assertTrue("Expected a reason why this move is invalid", optionalReason.isPresent());
        assertEquals("Expected reasoning that the game is complete",
                "Game [1] already completed", optionalReason.get().getReason());
    }

    @Test
    public void testValidatePositionAlreadyTaken() {
        Game game = new Game(2L, State.ONGOING, INCOMPLETE_BOARD, MOVES);
        Move move = new Move(2L, Player.X, 2, 0);

        Optional<InvalidityReason> optionalReason = Mover.validate(move, game);
        assertTrue("Expected a reason why this move is invalid", optionalReason.isPresent());
        assertEquals("Expected reasoning that the position is occupied",
                "Player X already at coordinates (2, 0) in game [2]", optionalReason.get().getReason());
    }

    @Test
    public void testValidateValid() {
        Game game = new Game(3L, State.ONGOING, INCOMPLETE_BOARD, MOVES);
        Move move = new Move(3L, Player.X, 0, 2);

        Optional<InvalidityReason> optionalReason = Mover.validate(move, game);
        assertFalse("Expected no reason why this move is invalid", optionalReason.isPresent());
    }

    @Test
    public void testMoveNotGameWinning() {
        Game game = new Game(4L, State.ONGOING, INCOMPLETE_BOARD, MOVES);
        Move move = new Move(4L, Player.X, 0, 1);

        Game newGame = Mover.move(move, game);
        assertEquals("Expected game to keep id", game.getId(), newGame.getId());
        assertEquals("Expected game state to remain ONGOING", State.ONGOING, newGame.getState());
        assertEquals("Expected X to be at [0,1]", Player.X, newGame.getBoard().getPlayerAtPosition(0, 1).get());
        assertEquals("Expected a fifth move in list", 5, newGame.getMoves().size());
        assertEquals("Expected new move to be last in list", move, Iterables.getLast(newGame.getMoves()));
    }

    @Test
    public void testMoveGameWinningX() {
        Game game = new Game(4L, State.ONGOING, INCOMPLETE_BOARD, MOVES);
        Move move = new Move(4L, Player.X, 1, 0);

        Game newGame = Mover.move(move, game);
        assertEquals("Expected game to keep id", game.getId(), newGame.getId());
        assertEquals("Expected game state to be X_VICTORY", State.X_VICTORY, newGame.getState());
        assertEquals("Expected X to be at [1,0]", Player.X, newGame.getBoard().getPlayerAtPosition(1, 0).get());
        assertEquals("Expected a fifth move in list", 5, newGame.getMoves().size());
        assertEquals("Expected new move to be last in list", move, Iterables.getLast(newGame.getMoves()));
    }

    @Test
    public void testMoveGameWinningO() {
        Game game = new Game(4L, State.ONGOING, INCOMPLETE_BOARD, MOVES);
        Move move = new Move(4L, Player.O, 1, 0);

        Game newGame = Mover.move(move, game);
        assertEquals("Expected game to keep id", game.getId(), newGame.getId());
        assertEquals("Expected game state to be X_VICTORY", State.O_VICTORY, newGame.getState());
        assertEquals("Expected O to be at [1,0]", Player.O, newGame.getBoard().getPlayerAtPosition(1, 0).get());
        assertEquals("Expected a fifth move in list", 5, newGame.getMoves().size());
        assertEquals("Expected new move to be last in list", move, Iterables.getLast(newGame.getMoves()));
    }

    @Test
    public void testMoveGameDrawing() {
        Player[][] almostFull = {{Player.O, Player.O, Player.X},
                                 {Player.X, Player.X, Player.O},
                                 {Player.O, null, Player.X}};
        List<Move> moves = ImmutableList.of(new Move(1L, Player.X, 1, 1),
                                            new Move(1L, Player.O, 0, 0),
                                            new Move(1L, Player.X, 0, 1),
                                            new Move(1L, Player.O, 2, 1),
                                            new Move(1L, Player.X, 2, 0),
                                            new Move(1L, Player.O, 0, 2),
                                            new Move(1L, Player.X, 2, 2),
                                            new Move(1L, Player.O, 1, 0));

        Game game = new Game(1L, State.ONGOING, new Board(almostFull), moves);
        Move move = new Move(1L, Player.X, 1, 2);

        Game newGame = Mover.move(move, game);
        assertEquals("Expected game to keep id", game.getId(), newGame.getId());
        assertEquals("Expected game state to be CATS_GAME", State.CATS_GAME, newGame.getState());
        assertEquals("Expected X to be at [1,2]", Player.X, newGame.getBoard().getPlayerAtPosition(1, 2).get());
        assertEquals("Expected a ninth move in list", 9, newGame.getMoves().size());
        assertEquals("Expected new move to be last in list", move, Iterables.getLast(newGame.getMoves()));
    }

    @Test
    public void testMoveFirstMove() {
        Game game = new Game();
        Move move = new Move(1L, Player.X, 1, 1);

        Game newGame = Mover.move(move, game);
        assertEquals("Expected game to keep id", game.getId(), newGame.getId());
        assertEquals("Expected game state to be ONGOING", State.ONGOING, newGame.getState());
        assertEquals("Expected X to be at [1,1]", Player.X, newGame.getBoard().getPlayerAtPosition(1, 1).get());
        assertEquals("Expected only one move in list", 1, newGame.getMoves().size());
        assertEquals("Expected new move to be only move in list", move, Iterables.getOnlyElement(newGame.getMoves()));
    }
}
