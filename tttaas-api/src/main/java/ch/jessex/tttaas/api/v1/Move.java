package ch.jessex.tttaas.api.v1;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a unique move within the game.
 *
 * @author jessex
 * @since 0.0.1
 */
public final class Move {
    private final long id;
    private final Player player;
    private final int x;
    private final int y;
    private final InvalidityReason invalidityReason;

    /**
     * Constructs a Move that is assumed to be valid.
     * @param id the id of the move
     * @param player the player making the move
     * @param x the x coordinate of the move
     * @param y the y coordinate of the move
     */
    public Move(long id, Player player, int x, int y) {
        this(id, player, x, y, null);
    }

    public Move(long id, Player player, int x, int y, InvalidityReason invalidityReason) {
        checkArgument(x >= 0 && x <= 2, "x must be between 0 and 2 (inclusive)");
        checkArgument(y >= 0 && y <= 2, "y must be between 0 and 2 (inclusive)");

        this.id = id;
        this.player = checkNotNull(player, "player cannot be null");
        this.x = x;
        this.y = y;
        this.invalidityReason = invalidityReason;
    }

    /**
     * Returns a new move with the given invalidity reason, otherwise identical to this move.
     * @param invalidityReason the reason the move is invalid
     * @return a copy of this move with an invalidity reason
     */
    public Move withInvalidityReason(InvalidityReason invalidityReason) {
        return new Move(this.id, this.player, this.x, this.y,
                checkNotNull(invalidityReason, "invalidityReason cannot be null"));
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return the reason that this move was invalid; null if it was valid
     */
    public InvalidityReason getInvalidityReason() {
        return invalidityReason;
    }

    /**
     * The reason why some move is invalid.
     *
     * @author jessex
     * @since 0.0.1
     */
    public static final class InvalidityReason {
        private final String reason;

        public InvalidityReason(String reason) {
            checkArgument(!StringUtils.isBlank(reason), "reason cannot be blank, empty, or null");
            this.reason = reason;
        }

        public InvalidityReason(String reason, Object... args) {
            checkArgument(!StringUtils.isBlank(reason), "reason cannot be blank empty or null");
            this.reason = String.format(reason, args);
        }

        public String getReason() {
            return reason;
        }
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
