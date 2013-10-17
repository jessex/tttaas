package ch.jessex.tttaas.db.jdbi;

/**
 * A POJO for mapping a row of the result set from a join over the game and move tables.
 *
 * @author jessex
 * @since 0.0.1
 */
public class GameMoveJoinRow {
    private long gameId;
    private String state;
    private String board;

    private long moveId;
    private String player;
    private int x;
    private int y;


    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public long getMoveId() {
        return moveId;
    }

    public void setMoveId(long moveId) {
        this.moveId = moveId;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}