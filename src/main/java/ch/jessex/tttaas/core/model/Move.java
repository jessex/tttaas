package ch.jessex.tttaas.core.model;

import org.apache.commons.lang.StringUtils;

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
    private final int xCoordinate;
    private final int yCoordinate;
    private final InvalidityReason invalidityReason;

    /**
     * Constructs a Move that is assumed to be valid.
     * @param id the id of the move
     * @param player the player making the move
     * @param xCoordinate the x coordinate of the move
     * @param yCoordinate the y coordinate of the move
     */
    public Move(long id, Player player, int xCoordinate, int yCoordinate) {
        this(id, player, xCoordinate, yCoordinate, null);
    }

    public Move(long id, Player player, int xCoordinate, int yCoordinate, InvalidityReason invalidityReason) {
        checkArgument(xCoordinate >= 0 && xCoordinate <= 2, "xCoordinate must be between 0 and 2 (inclusive)");
        checkArgument(yCoordinate >= 0 && yCoordinate <= 2, "yCoordinate must be between 0 and 2 (inclusive)");

        this.id = id;
        this.player = checkNotNull(player, "player cannot be null");
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.invalidityReason = invalidityReason;
    }

    /**
     * Returns a new move with the given invalidity reason, otherwise identical to this move.
     * @param invalidityReason the reason the move is invalid
     * @return a copy of this move with an invalidity reason
     */
    public Move withInvalidityReason(InvalidityReason invalidityReason) {
        return new Move(this.id, this.player, this.xCoordinate, this.yCoordinate,
                checkNotNull(invalidityReason, "invalidityReason cannot be null"));
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
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
}
