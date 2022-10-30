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
}
