package ch.jessex.tttaas.resources;

import java.util.concurrent.atomic.AtomicLong;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.jessex.tttaas.core.Mover;
import ch.jessex.tttaas.core.model.Game;
import ch.jessex.tttaas.core.model.Move;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The resource backing {@link Move}-related endpoints.
 *
 * @author jessex
 * @since 0.0.1
 */
@Path("/move/{gameId}")
@Produces(MediaType.APPLICATION_JSON)
public class MoveResource {
    private static final Logger LOG = LoggerFactory.getLogger(MoveResource.class);

    private final AtomicLong moveCounter;

    public MoveResource() {
        this.moveCounter = new AtomicLong();
    }

    /**
     * Applies the move described by the given query parameters--player, x, and y--ot the game with the given gameId
     * path parameter.
     *
     * @param gameId the game to apply the move to
     * @param player the player making the move
     * @param x the x coordinate of the move
     * @param y the y coordinate of the move
     * @return the new Game with the move applied
     */
    @POST
    @Timed
    public Game postMove(@PathParam("gameId") LongParam gameId,
                         @QueryParam("player") Optional<PlayerParam> player,
                         @QueryParam("x") Optional<IntParam> x,
                         @QueryParam("y") Optional<IntParam> y) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!player.isPresent()) {
            LOG.debug("postMove call for game [{}] made without player parameter", gameId);
            stringBuilder.append("Must provide 'player' query parameter with value X or O. ");
        }
        if (!x.isPresent()) {
            LOG.debug("postMove call for game [{}] made without x parameter", gameId);
            stringBuilder.append("Must provide 'x' query parameter with x coordinate of move. ");
        }
        if (!y.isPresent()) {
            LOG.debug("postMove call for game [{}] made without y parameter", gameId);
            stringBuilder.append("Must provide 'y' query parameter with y coordinate of move. ");
        }

        if (stringBuilder.length() != 0) {
            throw new WebApplicationException(badRequest(stringBuilder.toString(), MediaType.TEXT_PLAIN_TYPE));
        }

        // Once support for JDBI exists, retrieve the game by gameId and use that here. For now...
        Game game = new Game(gameId.get());

        Move move;
        try {
            move = new Move(this.moveCounter.incrementAndGet(), player.get().get(), x.get().get(), y.get().get());
        }
        catch(IllegalArgumentException ex) {
            throw new WebApplicationException(badRequest(ex.getMessage(), MediaType.TEXT_PLAIN_TYPE));
        }

        Optional<Move.InvalidityReason> optionalReason = Mover.validate(move, game);
        if (optionalReason.isPresent()) {
            throw new WebApplicationException(badRequest(optionalReason.get(), MediaType.APPLICATION_JSON_TYPE));
        }

        return Mover.move(move, game);
    }

    private Response badRequest(Object entity, MediaType mediaType) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(entity)
                       .type(mediaType)
                       .build();
    }
}
