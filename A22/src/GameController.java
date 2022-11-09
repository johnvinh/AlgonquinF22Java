/*
Name: John Vinh
Course: CST8221
Assignment: A12
Professor: Paulo Sousa
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController extends JFrame {
    private GameModel model;
    private GameView view;
    private Timer timer;

    GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    public int getDim() {
        return model.getDim();
    }

    public static String dimToSolution(int dim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < dim*dim; i++) {
            sb.append(i);
            sb.append("_");
        }
        return sb.toString();
    }

    public void initialize() {
        model.setBoard(view.initializeView(model.getDim(), new DimBoxListener(), dimToSolution(model.getDim()), model));
        view.playMode.addActionListener(new PlayButtonClicked());
        view.typeChoice.addActionListener(new TypeChoiceListener());
        JButton[][] board = model.getBoard();
        for (int i = 0; i < model.getDim(); i++) {
            for (int j = 0; j < model.getDim(); j++) {
                board[i][j].addActionListener(new GameButtonListener());
            }
        }

        // Buttons
        view.setDesignButton.addActionListener(new SetDesignListener());
        view.finishButton.addActionListener(new FinishButtonListener());
        view.resetButton.addActionListener(new ResetButtonListener());
        view.showButton.addActionListener(new ShowButtonListener());

        // Menu items
        view.newMenuItem.addActionListener(new NewMenuItemListener());
        view.colorsMenuItem.addActionListener(new ColorsMenuItemListener());
        view.solutionMenuItem.addActionListener(new SolutionMenuItemListener());
    }

    private class DimBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            int newDim = Integer.parseInt(String.valueOf(source.getSelectedItem()));
            model.setDim(newDim);

            // there should be dim*dim - 1 buttons, 1 space is left empty
            model.setBoard(view.setupBoard(model.getDim(), dimToSolution(model.getDim()), model));
            JButton[][] board = model.getBoard();
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    board[i][j].addActionListener(new GameButtonListener());
                }
            }

            view.mainGamePanel.revalidate();
            view.mainGamePanel.repaint();
        }
    }

    private class PlayButtonClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Start the timer
            timer = new Timer(1000, new TimeIncrementer());
            timer.start();

            // Disable some buttons
            view.showButton.setEnabled(false);
            view.hideButton.setEnabled(false);
            view.typeChoice.setEnabled(false);
            view.playMode.setEnabled(false);
            view.dimComoBox.setEnabled(false);

            // Check if each tile is correct
            JButton[][] board = model.getBoard();
            String[][] solution = model.getSolution();
            int initialScore = 0;
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    if (!(board[i][j].isEnabled())) {
                        continue;
                    }
                    if (board[i][j].getText().equals(solution[i][j])) {
                        initialScore++;
                        board[i][j].setBackground(model.getCorrectColor());
                    } else {
                        board[i][j].setBackground(model.getIncorrectColor());
                    }
                }
            }
            model.setScore(initialScore);
            view.pointsCountLabel.setText(String.valueOf(initialScore));
            model.setMode("Play");
        }
    }

    private class TimeIncrementer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int time = model.getTimeElapsed();
            time++;
            view.timeElapsed.setText(String.valueOf(time));
            model.setTimeElapsed(time);
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

    private void swapButtons(JButton button1, JButton button2, String mode, int newRow, int newCol) {
        String temp = button1.getText();
        button2.setEnabled(true);
        button2.setText(temp);
        button2.setBackground(Color.WHITE);

        button1.setEnabled(false);
        button1.setText("");
        button1.setBackground(Color.BLACK);

        if (mode.equals("Play")) {
            int currentMoves = model.getMoves();
            view.movesCountLabel.setText(String.valueOf(currentMoves + 1));
            model.setMoves(currentMoves + 1);
            String[][] solution = model.getSolution();
            // Button 2 is the new position of the button
            if (button2.getText().equals(solution[newRow][newCol])) {
                button2.setBackground(model.getCorrectColor());
            } else {
                button2.setBackground(model.getIncorrectColor());
            }

            // Get new score
            JButton[][] board = model.getBoard();
            int score = 0;
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    if (board[i][j].getText().equals(solution[i][j])) {
                        score++;
                    }
                }
            }
            view.pointsCountLabel.setText(String.valueOf(score));
            model.setScore(score);
        }
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
                    swapButtons(board[row][col], board[emptySpaceRow][emptySpaceCol], model.getMode(), emptySpaceRow, emptySpaceCol);
                }
            } else if (col == emptySpaceCol) { // Same column, different row
                if (row == (emptySpaceRow - 1) || row == (emptySpaceRow + 1)) {
                    swapButtons(board[row][col], board[emptySpaceRow][emptySpaceCol], model.getMode(), emptySpaceRow, emptySpaceCol);
                }
            }
        }
    }

    private class SetDesignListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int dim = model.getDim();
            int maxTiles = dim * dim - 1;
            int numTiles = 0;
            String inputSolution = view.designText.getText();
            StringBuilder sb = new StringBuilder();

            // Make sure we don't add more than the max tiles
            for (int i = 0; i < inputSolution.length(); i++) {
                if ((numTiles + 1) > maxTiles) {
                    break;
                }
                sb.append(inputSolution.charAt(i));
                sb.append("_");
                numTiles++;
            }

            // Pad with spaces in case the solution is too short for the dimension
            if (numTiles < maxTiles) {
                int difference = maxTiles - numTiles;
                for (int i = 0; i < difference; i++) {
                    sb.append(" ");
                    sb.append("_");
                }
            }
            // there should be dim*dim - 1 buttons, 1 space is left empty
            model.setBoard(view.setupBoard(dim, sb.toString(), model));
            JButton[][] board = model.getBoard();
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    board[i][j].addActionListener(new GameButtonListener());
                }
            }

            view.mainGamePanel.revalidate();
            view.mainGamePanel.repaint();
        }
    }

    private class FinishButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int currentPoints = model.getScore();
            int maxPoints = model.getDim() * model.getDim() - 1;
            if (currentPoints == maxPoints) {
                new GameView.GameWinner();
            } else {
                new GameView.GameFinish();
            }
            timer.stop();
        }
    }

    private void reset() {
        timer.stop();

        // Model
        model.setScore(0);
        model.setMode("Design");
        model.setMoves(0);
        model.setTimeElapsed(0);

        // View
        view.pointsCountLabel.setText("0");
        view.designButton.setSelected(true);
        view.movesCountLabel.setText("0");
        view.timeElapsed.setText("0");
        view.playMode.setEnabled(true);
        view.dimComoBox.setEnabled(true);
        view.showButton.setEnabled(true);
        view.hideButton.setEnabled(true);
        view.typeChoice.setEnabled(true);
        view.typeChoice.setSelectedIndex(0);
        view.setDesignButton.setEnabled(false);
        view.designText.setEnabled(false);
    }

    private class NewMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = view.dimComoBox;
            int newDim = Integer.parseInt(String.valueOf(source.getSelectedItem()));
            model.setDim(newDim);

            // there should be dim*dim - 1 buttons, 1 space is left empty
            model.setBoard(view.setupBoard(model.getDim(), dimToSolution(model.getDim()), model));
            JButton[][] board = model.getBoard();
            for (int i = 0; i < model.getDim(); i++) {
                for (int j = 0; j < model.getDim(); j++) {
                    board[i][j].addActionListener(new GameButtonListener());
                }
            }

            view.mainGamePanel.revalidate();
            view.mainGamePanel.repaint();

            reset();
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();
        }
    }

    private class ColorsMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Color color1 = model.getCorrectColor();
            Color color2 = model.getIncorrectColor();
            GameView.ColorChanger colorChanger = new GameView.ColorChanger(color1, color2);
            // Open up a new window to pick the color
            colorChanger.setColor1Button.addActionListener(e1 -> {
                GameView.ColorPicker colorPicker = new GameView.ColorPicker(color1, model);
                // When the selected color changes, set it in model
                colorPicker.colorPicker.getSelectionModel().addChangeListener(e2 -> {
                    Color selectedColor = colorPicker.colorPicker.getColor();
                    model.setCorrectColor(selectedColor);

                    // Also adjust the color in our menu popup
                    colorChanger.setButton1Color(selectedColor);
                });
            });
            // Open up a new window to pick the color
            colorChanger.setColor2Button.addActionListener(e12 -> {
                GameView.ColorPicker colorPicker = new GameView.ColorPicker(color1, model);
                // When the selected color changes, set it in model
                colorPicker.colorPicker.getSelectionModel().addChangeListener(e2 -> {
                    Color selectedColor = colorPicker.colorPicker.getColor();
                    model.setIncorrectColor(selectedColor);

                    // Also adjust the color in our menu popup
                    colorChanger.setButton2Color(selectedColor);
                });
            });
        }
    }

    private class SolutionMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Color correctColor = model.getCorrectColor();
            JButton[][] board = model.getBoard();
            String[][] solution = model.getSolution();
            int dim = model.getDim();
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    // The empty/black button
                    if (i == (dim - 1) && j == (dim - 1)) {
                        board[i][j].setText("");
                        board[i][j].setBackground(Color.BLACK);
                        board[i][j].setEnabled(false);
                        continue;
                    }
                    board[i][j].setText(solution[i][j]);
                    board[i][j].setBackground(correctColor);
                    board[i][j].setEnabled(true);
                }
            }
        }
    }

    private class ShowButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton[][] board = model.getBoard();
            String[][] solution = model.getSolution();
            int dim = model.getDim();
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    // The empty/black button
                    if (i == (dim - 1) && j == (dim - 1)) {
                        board[i][j].setText("");
                        board[i][j].setBackground(Color.BLACK);
                        board[i][j].setEnabled(false);
                        continue;
                    }
                    board[i][j].setText(solution[i][j]);
                    board[i][j].setEnabled(true);
                }
            }
        }
    }
}
