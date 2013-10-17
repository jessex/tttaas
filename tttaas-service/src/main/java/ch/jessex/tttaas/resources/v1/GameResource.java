package ch.jessex.tttaas.resources.v1;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.db.GameDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The resource backing {@link Game}-related endpoints.
 *
 * @author jessex
 * @since 0.0.1
 */
@Path("/v1/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
    private static final Logger LOG = LoggerFactory.getLogger(GameResource.class);

    private final GameDAO gameDAO;

    public GameResource(GameDAO gameDAO) {
        this.gameDAO = checkNotNull(gameDAO, "gameDAO cannot be null");
    }

    @POST
    @Timed
    public Game createGame() {
        Game game = this.gameDAO.upsert(new Game());
        LOG.info("Creating new game with id [{}]", game.getId());
        return game;
    }

    @GET
    @Timed
    @Path("/{gameId}")
    public Game getGameById(@PathParam("gameId") LongParam gameId) {
        long id = gameId.get();
        LOG.info("Retrieving game with id [{}]", id);

        Optional<Game> optionalGame = this.gameDAO.findById(id);

        if (!optionalGame.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                                                      .entity(String.format("No game found with id [%s]", id))
                                                      .type(MediaType.TEXT_PLAIN_TYPE)
                                                      .build());
        }

        return optionalGame.get();
    }
}
