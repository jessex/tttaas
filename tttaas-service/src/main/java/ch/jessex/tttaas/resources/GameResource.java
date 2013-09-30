package ch.jessex.tttaas.resources;

import java.util.concurrent.atomic.AtomicLong;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.jessex.tttaas.api.v1.Game;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The resource backing {@link Game}-related endpoints.
 *
 * @author jessex
 * @since 0.0.1
 */
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
    private static final Logger LOG = LoggerFactory.getLogger(GameResource.class);

    private final AtomicLong gameCounter;

    public GameResource() {
        this.gameCounter = new AtomicLong();
    }

    @GET
    @Timed
    public Game getNewGame() {
        Game game = new Game(this.gameCounter.incrementAndGet());
        LOG.info("Creating new game with id [{}]", game.getId());
        return game;
    }
}
