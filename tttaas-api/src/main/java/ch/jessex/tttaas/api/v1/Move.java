package ch.jessex.tttaas.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.validation.ValidationMethod;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a unique move within the game.
 *
 * @author jessex
 * @since 0.0.1
 */
public final class Move {

    @NotEmpty
    private final String player;

    @Range(min = 0, max = 2)
    private final int x;

    @Range(min = 0, max = 2)
    private final int y;

    /**
     * The json-specific constructor which assumes automatic, annotation-driven validation and allows for friendly error
     * handling.
     *
     * @param player the player, as a case-insensitive string
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @JsonCreator
    protected Move(@JsonProperty("player") String player,
                   @JsonProperty("x") int x,
                   @JsonProperty("y") int y) {
        this.player = player.toUpperCase();
        this.x = x;
        this.y = y;
    }

    public Move(Player player, int x, int y) {
        checkArgument(x >= 0 && x <= 2, "x must be between 0 and 2 (inclusive)");
        checkArgument(y >= 0 && y <= 2, "y must be between 0 and 2 (inclusive)");

        this.player = checkNotNull(player, "player cannot be null").name();
        this.x = x;
        this.y = y;
    }


    public String getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @JsonIgnore
    @ValidationMethod(message="Player must be either X or O")
    public boolean isValidPlayer() {
        return Player.X.name().equals(this.player.toUpperCase()) || Player.O.name().equals(this.player.toUpperCase());
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
