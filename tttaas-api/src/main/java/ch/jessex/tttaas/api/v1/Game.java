package ch.jessex.tttaas.api.v1;

import java.util.List;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models an individual game of tic tac toe.
 *
 * @author jessex
 * @since 0.0.1
 */
public class Game {
    private final long id;
    private final State state;
    private final Board board;
    private final List<Move> moves;

    /**
     * Constructs a new game with no {@link Move moves} and with a dummy id that doesn't correspond to anything in the database.
     */
    public Game() {
        this(0, State.ONGOING, new Board(), null);
    }

    public Game(long id, State state, Board board, List<Move> moves) {
        this.id = id;
        this.state = checkNotNull(state, "state cannot be null");
        this.board = checkNotNull(board, "board cannot be null");

        if (moves == null) {
            this.moves = ImmutableList.of();
        }
        else {
            this.moves = ImmutableList.copyOf(moves);
        }
    }

    public long getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Board getBoard() {
        return board;
    }

    public List<Move> getMoves() {
        return moves;
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
