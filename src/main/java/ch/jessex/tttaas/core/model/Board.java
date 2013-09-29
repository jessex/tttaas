package ch.jessex.tttaas.core.model;

import java.util.Arrays;

import com.google.common.base.Optional;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models the current state of the board. The squares of the board are modeled as a 3x3 two dimensional array of
 * {@link Player Players}. The column of a square is denoted by its x coordinate, and a row by its y coordinate. This looks
 * roughly like:
 *
 *     x0   x1   x2
 * y0     |    |
 *    ----+----+----
 * y1     |    |
 *    ----+----+----
 * y2     |    |
 *
 * An empty board is filled with {@code null} values in each square; the board will always have dimensions of 3x3.
 *
 * @author jessex
 * @since 0.0.1
 */
public final class Board {

    private final Player[][] squares;

    /**
     * Constructs a blank board with no squares yet occupied. Each element will be initialized to {@code null}.
     */
    public Board() {
        this.squares = new Player[3][3];
    }

    public Board(Player[][] squares) {
        this.squares = squares;
    }

    /**
     * Returns a board with the given {@link Move} applied. This performs no validation as to whether the move is a valid one;
     * it assumes that such validation was performed beforehand.
     *
     * @param move the move to add to the board
     * @return a new board
     */
    public Board withNewMove(Move move) {
        checkNotNull(move, "move cannot be null");

        Player[][] newSquares = new Player[3][3];
        for (int i = 0; i < this.squares.length; i++) {
            newSquares[i] = Arrays.copyOf(this.squares[i], 3);
        }
        newSquares[move.getYCoordinate()][move.getXCoordinate()] = move.getPlayer();

        return new Board(newSquares);
    }

    public Optional<Player> getPlayerAtPosition(int x, int y) {
        if (this.squares[y][x] == null) {
            return Optional.absent();
        }
        return Optional.of(this.squares[y][x]);
    }

    public Player[][] getSquares() {
        return this.squares;
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
