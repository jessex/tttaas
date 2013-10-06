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
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

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
        // Once support for JDBI exists, retrieve the game by gameId and use that here. For now...
        Game game = new Game(gameId.get());

        Optional<InvalidityReason> optionalReason = Mover.validate(move, game);
        if (optionalReason.isPresent()) {
            throw new WebApplicationException(badRequest(optionalReason.get().getReason()));
        }

        return Mover.move(move, game);
    }

    private Response badRequest(Object entity) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(entity)
                       .type(MediaType.TEXT_PLAIN_TYPE)
                       .build();
    }
}
