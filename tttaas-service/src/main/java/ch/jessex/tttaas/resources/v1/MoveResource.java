package ch.jessex.tttaas.resources.v1;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.jessex.tttaas.api.v1.Game;
import ch.jessex.tttaas.api.v1.InvalidityReason;
import ch.jessex.tttaas.api.v1.Move;
import ch.jessex.tttaas.core.Mover;
import ch.jessex.tttaas.db.GameDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The resource backing {@link Move}-related endpoints.
 *
 * @author jessex
 * @since 0.0.1
 */
@Path("/v1/move/{gameId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MoveResource {
    private static final Logger LOG = LoggerFactory.getLogger(MoveResource.class);

    private final GameDAO gameDAO;

    public MoveResource(GameDAO gameDAO) {
        this.gameDAO = checkNotNull(gameDAO, "gameDAO cannot be null");
    }

    /**
     * Applies the move described by the given query parameters--player, x, and y--ot the game with the given gameId
     * path parameter.
     *
     * @param gameId the game to apply the move to
     * @param move the move to make
     * @return the new Game with the move applied
     */
    @POST
    @Timed
    public Game postMove(@PathParam("gameId") LongParam gameId,
                         @Valid Move move) {
        long id = gameId.get();
        LOG.info("Attempting to post move [{}] to game with id [{}]", move, id);

        Optional<Game> optionalGame = this.gameDAO.findById(id);
        if (!optionalGame.isPresent()) {
            String error = String.format("No game found with id [%s]", id);
            LOG.info(error);
            throw new WebApplicationException(badRequest(Response.Status.NOT_FOUND, error));
        }

        Game game = optionalGame.get();

        Optional<InvalidityReason> optionalReason = Mover.validate(move, game);
        if (optionalReason.isPresent()) {
            throw new WebApplicationException(badRequest(Response.Status.BAD_REQUEST, optionalReason.get().getReason()));
        }

        return this.gameDAO.upsert(Mover.move(move, game));
    }

    private Response badRequest(Response.Status status, Object entity) {
        return Response.status(status)
                       .entity(entity)
                       .type(MediaType.TEXT_PLAIN_TYPE)
                       .build();
    }
}
