package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.WebApplicationException;

import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.api.v1.Player;
import ch.jessex.tttaas.core.Mover;
import com.yammer.dropwizard.jersey.params.LongParam;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MoveResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestMoveResource {

    private MoveResource moveResource = new MoveResource();

    @Rule
    public ExpectedException testRuleExpectedException = ExpectedException.none();

    @Test
    public void testPostBadGameId() {
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.moveResource.postMove(new LongParam("x"), new Move(Player.O, 1, 1));
    }

    @Test
    public void testPostHappyPath() {
        Move move = new Move(Player.O, 2, 2);
        Game game = this.moveResource.postMove(new LongParam("4"), move);

        Game expectedGame = Mover.move(move, new Game(4L));
        assertEquals("Unexpected game response", expectedGame, game);
    }
}
