package ch.jessex.tttaas.resources;

import ch.jessex.tttaas.api.v1.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GameResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestGameResource {

    private GameResource gameResource = new GameResource();

    @Test
    public void testGetNewGame() {
        Game firstGame = this.gameResource.getNewGame();
        assertEquals("Should have received a new game with id 1", new Game(1L), firstGame);

        Game secondGame = this.gameResource.getNewGame();
        assertEquals("Should have received a new game with id 2", new Game(2L), secondGame);
    }
}
