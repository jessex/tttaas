package ch.jessex.tttaas.api.v1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Board}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestBoard {
    private static final Player[][] SQUARES = {{Player.O, Player.X, null},
                                               {null, Player.X, null},
                                               {null, Player.O, null}};

    @Rule
    public ExpectedException testRuleException = ExpectedException.none();

    @Test
    public void testWithNewMoveNullMove() {
        this.testRuleException.expect(NullPointerException.class);
        this.testRuleException.expectMessage("move cannot be null");
        new Board(SQUARES).withNewMove(null);
    }

    @Test
    public void testWithNewMove() {
        Board board = new Board(SQUARES);
        Board newBoard = board.withNewMove(new Move(Player.X, 0, 2));
        assertEquals("Should have placed X at [0,2]", Player.X, newBoard.getPlayerAtPosition(0, 2).get());

        newBoard = newBoard.withNewMove(new Move(Player.O, 2, 0));
        assertEquals("Should have placed O at [2,0]", Player.O, newBoard.getPlayerAtPosition(2, 0).get());
        assertEquals("X should still be at [0,2]", Player.X, newBoard.getPlayerAtPosition(0, 2).get());
    }

    @Test
    public void testWithNewMoveFirstMove() {
        Board board = new Board();
        Board newBoard = board.withNewMove(new Move(Player.X, 1, 1));
        assertEquals("Should have placed X at [1,1]", Player.X, newBoard.getPlayerAtPosition(1, 1).get());
    }

    @Test
    public void testGetPlayerAtPositionAbsent() {
        Board board = new Board(SQUARES);
        assertFalse("Expected no player at [2,2]", board.getPlayerAtPosition(2, 2).isPresent());
    }

    @Test
    public void testGetPlayerAtPositionPresent() {
        Board board = new Board(SQUARES);
        assertEquals("Expected X at [1,0]", Player.X, board.getPlayerAtPosition(1, 0).get());
        assertEquals("Expected O at [1,2]", Player.O, board.getPlayerAtPosition(1, 2).get());
    }

    @Test
    public void testGetPlayerAtPositionEmptyBoard() {
        Board board = new Board();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                assertFalse(String.format("Expected no player at [%d, %d]", x, y), board.getPlayerAtPosition(x, y).isPresent());
            }
        }
    }
}
