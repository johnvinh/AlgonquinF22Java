import javax.swing.*;

public class GameModel {
    private String configuration;
    /**
     * An integer specifying the size of the game board
     */
    private int dim = 0;
    /**
     * A JButton array containing the buttons for the gameplay
     */
    private JButton[][] board;
    private int moves = 0;
    private int timeElapsed = 0;
    private int score = 0;
    private String mode = "Design";

    public GameModel() {
        this.dim = 3;
        this.board = new JButton[dim][dim];
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public void setBoard(JButton[][] board) {
        this.board = board;
    }

    public JButton[][] getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
