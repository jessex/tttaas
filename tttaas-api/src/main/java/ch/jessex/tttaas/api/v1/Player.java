package ch.jessex.tttaas.api.v1;

/**
 * An enumeration of the various possible players, ie. X and O.
 *
 * @author jessex
 * @since 0.0.1
 */
public enum Player {
    X('X'),
    O('O');

    private final char letter;

    Player(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }
}
