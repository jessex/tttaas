package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.api.v1.Player;
import ch.jessex.tttaas.core.Mover;
import ch.jessex.tttaas.resources.params.PlayerParam;
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
        try {
            this.moveResource.postMove(new LongParam("4"), Optional.<PlayerParam>absent(),
                    Optional.of(new IntParam("2")), Optional.of(new IntParam("2")));
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST,
                    "Must provide 'player' query parameter with value X or O. ");
        }
    }

    @Test
    public void testPostMoveMissingX() {
        try {
            this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("x")),
                    Optional.<IntParam>absent(), Optional.of(new IntParam("2")));
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST,
                    "Must provide 'x' query parameter with x coordinate of move. ");
        }
    }

    @Test
    public void testPostMoveMissingY() {
        try {
            this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("x")),
                    Optional.of(new IntParam("2")), Optional.<IntParam>absent());
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST,
                    "Must provide 'y' query parameter with y coordinate of move. ");
        }
    }

    @Test
    public void testPostMissingAll() {
        try {
            this.moveResource.postMove(new LongParam("4"), Optional.<PlayerParam>absent(),
                    Optional.<IntParam>absent(), Optional.<IntParam>absent());
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST,
                    "Must provide 'player' query parameter with value X or O. " +
                    "Must provide 'x' query parameter with x coordinate of move. " +
                    "Must provide 'y' query parameter with y coordinate of move. ");
        }
    }

    @Test
    public void testPostMoveBadCoordinates() {
        try {
            this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("O")),
                    Optional.of(new IntParam("3")), Optional.of(new IntParam("2")));
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST, "x must be between 0 and 2 (inclusive)");
        }

        try {
            this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("X")),
                    Optional.of(new IntParam("0")), Optional.of(new IntParam("-1")));
        }
        catch (WebApplicationException ex) {
            assertResponse(ex.getResponse(), Response.Status.BAD_REQUEST, "y must be between 0 and 2 (inclusive)");
        }
    }

    @Test
    public void testPostHappyPath() {
        Game game = this.moveResource.postMove(new LongParam("4"), Optional.of(new PlayerParam("O")),
                Optional.of(new IntParam("2")), Optional.of(new IntParam("2")));

        Game expectedGame = Mover.move(new Move(1L, Player.O, 2, 2), new Game(4L));
        assertEquals("Unexpected game response", expectedGame, game);
    }

    private void assertResponse(Response response, Response.Status expectedStatus, Object expectedEntity) {
        assertEquals("Unexpected response status", expectedStatus.getStatusCode(), response.getStatus());
        assertEquals("Unexpected response entity", expectedEntity, response.getEntity());
    }
}
