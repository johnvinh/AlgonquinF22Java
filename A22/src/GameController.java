/*
Name: John Vinh
Course: CST8221
Assignment: A12
Professor: Paulo Sousa
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController extends JFrame {
    private GameModel model;
    private GameView view;

    GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    public int getDim() {
        return model.getDim();
    }

    public void initialize() {
        model.setBoard(view.initializeView(model.getDim(), new DimBoxListener()));
        view.playMode.addActionListener(new PlayButtonClicked());
        view.typeChoice.addActionListener(new TypeChoiceListener());
        JButton[][] board = model.getBoard();
        for (int i = 0; i < model.getDim(); i++) {
            for (int j = 0; j < model.getDim(); j++) {
                board[i][j].addActionListener(new GameButtonListener());
            }
        }
    }

    private class DimBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            int newDim = Integer.parseInt(String.valueOf(source.getSelectedItem()));
            model.setDim(newDim);

            // there should be dim*dim - 1 buttons, 1 space is left empty
            model.setBoard(view.setupBoard(model.getDim()));

            view.mainGamePanel.revalidate();
            view.mainGamePanel.repaint();
        }
    }

    private class PlayButtonClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Start the timer
            new Timer(1000, new TimeIncrementer()).start();

            // Disable some buttons
            view.showButton.setEnabled(false);
            view.hideButton.setEnabled(false);
            view.typeChoice.setEnabled(false);
            view.playMode.setEnabled(false);
        }
    }

    private class TimeIncrementer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int time = Integer.parseInt(view.timeElapsed.getText());
            time++;
            view.timeElapsed.setText(String.valueOf(time));
        }
    }

    private class TypeChoiceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            String type = String.valueOf(source.getSelectedItem());

            if (type.equals("Number")) {
                view.designText.setEnabled(false);
                view.setDesignButton.setEnabled(false);
            } else {
                view.designText.setEnabled(true);
                view.setDesignButton.setEnabled(true);
            }
        }
    }

    private void swapButtons(JButton button1, JButton button2) {
        String temp = button1.getText();
        button2.setEnabled(true);
        button2.setText(temp);
        button2.setBackground(Color.WHITE);

        button1.setEnabled(false);
        button1.setText("");
        button1.setBackground(Color.BLACK);
    }
    private class GameButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton[][] board = model.getBoard();
            JButton source = (JButton) e.getSource();
            String text = source.getText();
            // Find this button's position in the board and the empty space
            int row = 0;
            int col = 0;
            int emptySpaceRow = 0;
            int emptySpaceCol = 0;
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    if (board[i][j] == source) {
                        row = i;
                        col = j;
                    }
                    else if (!(board[i][j].isEnabled())) {
                        emptySpaceRow = i;
                        emptySpaceCol = j;
                    }
                }
            }

            // Same row, different column
            if (row == emptySpaceRow) {
                if (col == (emptySpaceCol - 1) || col == (emptySpaceCol + 1)) {
                    swapButtons(board[row][col], board[emptySpaceRow][emptySpaceCol]);
                }
            } else if (col == emptySpaceCol) { // Same column, different row
                if (row == (emptySpaceRow - 1) || row == (emptySpaceRow + 1)) {
                    swapButtons(board[row][col], board[emptySpaceRow][emptySpaceCol]);
                }
            }
        }
    }
}
