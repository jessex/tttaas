package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.WebApplicationException;

import ch.jessex.tttaas.api.v1.Game;
import com.yammer.dropwizard.jersey.params.LongParam;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GameResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestGameResource {

    private GameResource gameResource = new GameResource();

    @Rule
    public ExpectedException testRuleExpectedException = ExpectedException.none();

    @Test
    public void testCreateGame() {
        Game firstGame = this.gameResource.createGame();
        assertEquals("Should have received a new game with id 1", new Game(1L), firstGame);

        Game secondGame = this.gameResource.createGame();
        assertEquals("Should have received a new game with id 2", new Game(2L), secondGame);
    }

    @Test
    public void testGetGameById() {
        Game game = this.gameResource.getGameById(new LongParam("27"));
        assertEquals("Should have received a game with id 27", 27, game.getId());
    }

    @Test
    public void testGetGameByIdBadId() {
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.gameResource.getGameById(new LongParam("cj"));
    }
}
