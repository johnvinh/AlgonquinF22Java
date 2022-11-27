/*
Name: John Vinh
Course: CST8221
Assignment: A22
Professor: Paulo Sousa
 */

import javax.swing.*;
import java.awt.*;

/**
 * The game model, stores the data
 */
public class GameModel {
    private String[][] solution;
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
    private Color correctColor;
    private Color incorrectColor;

    public GameModel() {
        this.dim = 3;
        this.board = new JButton[dim][dim];
        this.correctColor = Color.GREEN;
        this.incorrectColor = Color.RED;
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

    public String[][] getSolution() {
        return solution;
    }

    public void setSolution(String[][] solution) {
        this.solution = solution;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCorrectColor(Color correctColor) {
        this.correctColor = correctColor;
    }

    public Color getCorrectColor() {
        return correctColor;
    }

    public void setIncorrectColor(Color incorrectColor) {
        this.incorrectColor = incorrectColor;
    }

    public Color getIncorrectColor() {
        return incorrectColor;
    }
}
