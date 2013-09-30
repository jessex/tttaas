package ch.jessex.tttaas.resources;

import ch.jessex.tttaas.core.model.Player;
import com.yammer.dropwizard.jersey.params.AbstractParam;

/**
 * A parameter encapsulating {@link Player} values.
 *
 * @author jessex
 * @since 0.0.1
 */
public class PlayerParam extends AbstractParam<Player> {

    public PlayerParam(String param) {
        super(param);
    }

    @Override
    protected String errorMessage(String input, Exception e) {
        return String.format("Invalid player parameter [%s], must be X or O", input);
    }

    @Override
    protected Player parse(String input) throws Exception {
        return Player.valueOf(input.toUpperCase());
    }
}
