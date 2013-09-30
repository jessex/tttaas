package ch.jessex.tttaas.resources;

import javax.ws.rs.core.Response;

import ch.jessex.tttaas.core.Mover;
import ch.jessex.tttaas.core.model.Game;
import ch.jessex.tttaas.core.model.Move;
import ch.jessex.tttaas.core.model.Player;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.dropwizard.jersey.params.LongParam;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MoveResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestMoveResource {

    private MoveResource moveResource;

    @Before
    public void setUp() {
        this.moveResource = new MoveResource();
    }

    @Test
    public void testPostMoveMissingPlayer() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.<PlayerParam>absent(),
                Optional.of(new IntParam("2")), Optional.of(new IntParam("2")));

        assertResponse(response, Response.Status.BAD_REQUEST, "Must provide 'player' query parameter with value X or O. ");
    }

    @Test
    public void testPostMoveMissingX() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("x")),
                Optional.<IntParam>absent(), Optional.of(new IntParam("2")));

        assertResponse(response, Response.Status.BAD_REQUEST, "Must provide 'x' query parameter with x coordinate of move. ");
    }

    @Test
    public void testPostMoveMissingY() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("x")),
                Optional.of(new IntParam("2")), Optional.<IntParam>absent());

        assertResponse(response, Response.Status.BAD_REQUEST, "Must provide 'y' query parameter with y coordinate of move. ");
    }

    @Test
    public void testPostMissingAll() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.<PlayerParam>absent(),
                Optional.<IntParam>absent(), Optional.<IntParam>absent());

        assertResponse(response, Response.Status.BAD_REQUEST, "Must provide 'player' query parameter with value X or O. " +
                "Must provide 'x' query parameter with x coordinate of move. " +
                "Must provide 'y' query parameter with y coordinate of move. ");
    }

    @Test
    public void testPostMoveBadCoordinates() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("O")),
                Optional.of(new IntParam("3")), Optional.of(new IntParam("2")));

        assertResponse(response, Response.Status.BAD_REQUEST, "x must be between 0 and 2 (inclusive)");

        response = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("X")),
                Optional.of(new IntParam("0")), Optional.of(new IntParam("-1")));

        assertResponse(response, Response.Status.BAD_REQUEST, "y must be between 0 and 2 (inclusive)");
    }

    @Test
    public void testPostHappyPath() {
        Response response = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("O")),
                Optional.of(new IntParam("2")), Optional.of(new IntParam("2")));

        Move move = new Move(1L, Player.O, 2, 2);
        Game game = new Game(4L);
        game = Mover.move(move, game);

        assertResponse(response, Response.Status.OK, game);
    }

    private void assertResponse(Response response, Response.Status expectedStatus, Object expectedEntity) {
        assertEquals("Unexpected response status", expectedStatus.getStatusCode(), response.getStatus());
        assertEquals("Unexpected response entity", expectedEntity, response.getEntity());
    }
}
