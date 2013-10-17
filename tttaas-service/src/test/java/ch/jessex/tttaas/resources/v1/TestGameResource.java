package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.WebApplicationException;

import ch.jessex.tttaas.api.v1.Game;
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
 * Unit tests for the {@link GameResource}.
 *
 * @author jessex
 * @since 0.0.1
 */
public class TestGameResource {

    private GameDAO gameDAO = createMock(GameDAO.class);
    private GameResource gameResource = new GameResource(this.gameDAO);

    @Rule
    public ExpectedException testRuleExpectedException = ExpectedException.none();

    @Test
    public void testCreateGame() {
        Game newGame = new Game();

        expect(this.gameDAO.upsert(new Game())).andReturn(newGame);
        replay(this.gameDAO);

        Game game = this.gameResource.createGame();
        assertSame("Should have received the newly created game", newGame, game);

        verify(this.gameDAO);
    }

    @Test
    public void testGetGameById() {
        Game foundGame = new Game();
        expect(this.gameDAO.findById(27L)).andReturn(Optional.of(foundGame));
        replay(this.gameDAO);

        Game game = this.gameResource.getGameById(new LongParam("27"));
        assertSame("Should have received the fetched game", foundGame, game);

        verify(this.gameDAO);
    }

    @Test
    public void testGetGameByIdBadId() {
        replay(this.gameDAO);
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.gameResource.getGameById(new LongParam("cj"));
    }

    @Test
    public void testGetGameByIdNonExistentId() {
        expect(this.gameDAO.findById(5L)).andReturn(Optional.<Game>absent());
        replay(this.gameDAO);
        this.testRuleExpectedException.expect(WebApplicationException.class);
        this.gameResource.getGameById(new LongParam("5"));
    }
}
