package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.WebApplicationException;

import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.api.v1.Player;
import ch.jessex.tttaas.core.Mover;
import ch.jessex.tttaas.db.GameDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link MoveResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestMoveResource {

    private GameDAO gameDAO = createMock(GameDAO.class);
    private MoveResource moveResource = new MoveResource(this.gameDAO);

    @Rule
    public ExpectedException testRuleExpectedException = ExpectedException.none();

    @Test
    public void testPostBadGameId() {
        replay(this.gameDAO);
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.moveResource.postMove(new LongParam("x"), new Move(1L, Player.O, 1, 1));
    }

    @Test
    public void testPostNonExistentGameId() {
        expect(this.gameDAO.findById(3L)).andReturn(Optional.<Game>absent());
        replay(this.gameDAO);
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.moveResource.postMove(new LongParam("3"), new Move(3L, Player.O, 1, 1));
    }

    @Test
    public void testPostHappyPath() {
        Move move = new Move(1L, Player.O, 2, 2);
        Game expectedGame = Mover.move(move, new Game());

        expect(this.gameDAO.findById(4L)).andReturn(Optional.of(new Game()));
        expect(this.gameDAO.upsert(expectedGame)).andReturn(expectedGame);
        replay(this.gameDAO);

        Game game = this.moveResource.postMove(new LongParam("4"), move);

        assertSame("Should have received upserted game", expectedGame, game);

        verify(this.gameDAO);
    }
}
