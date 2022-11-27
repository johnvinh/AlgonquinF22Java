/*
Name: John Vinh
Course: CST8221
Assignment: A22
Professor: Paulo Sousa
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The game controller
 */
public class GameController extends JFrame {
    /**
     * The model of the game
     */
    private GameModel model;
    /**
     * The view of the game
     */
    private GameView view;
    /**
     * The timer which increments the time elapsed during a game
     */
    private Timer timer;

    /**
     * Constructor for controller
     * @param model the game model
     * @param view  the game view
     */
    GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    /**
     * A helper which converts a dimension to a string solution
     * ie 3 becomes 12345678
     *
     * @param dim   the dimension
     * @return  the string version of the solution
     */
    public static String dimToSolution(int dim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < dim*dim; i++) {
            sb.append(i);
            sb.append("_");
        }
        return sb.toString();
    }

    /**
     * Add all action listeners to the board
     */
    public void initialize() {
        model.setBoard(view.initializeView(model.getDim(), new DimBoxListener(), dimToSolution(model.getDim()), model));
        view.playMode.addActionListener(new PlayButtonClicked());
        view.designButton.addActionListener(new DesignButtonClicked());
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
        view.hideButton.addActionListener(new HideButtonListener());
        view.saveButton.addActionListener(new SaveButtonListener());
        view.loadButton.addActionListener(new LoadButtonListener());

        // Menu items
        view.newMenuItem.addActionListener(new NewMenuItemListener());
        view.solutionMenuItem.addActionListener(new SolutionMenuItemListener());
        view.exitMenuItem.addActionListener(new ExitMenuItemListener());
        view.colorsMenuItem.addActionListener(new ColorsMenuItemListener());
    }

    /**
     * Listens to dim box selection changes
     */
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

    /**
     * Listens for the play radio button being clicked
     */
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
            view.designButton.setEnabled(false);
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

    private class DesignButtonClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setMode("Design");
        }
    }

    /**
     * Increments the time by 1 second
     */
    private class TimeIncrementer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int time = model.getTimeElapsed();
            time++;
            view.timeElapsed.setText(String.valueOf(time));
            model.setTimeElapsed(time);
        }
    }

    /**
     * Enable or disable the design text options based on choice
     */
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

    /**
     * Swap two game buttons
     * @param button1   the button which was clicked
     * @param button2   the empty space button
     * @param mode  the play mode
     * @param newRow    the row of the empty space button
     * @param newCol    the column of the empty space button
     */
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

    /**
     * Listens for clicks on each button on the game board
     */
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

    /**
     * Listens for the set design text button being clicked
     */
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

    /**
     * Listens for finish button being clicked
     * Shows a game finished screen
     */
    private class FinishButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int currentPoints = model.getScore();
            int maxPoints = model.getDim() * model.getDim() - 1;
            // Show a different screen based on the number of points
            if (currentPoints == maxPoints) {
                new GameView.GameWinner();
            } else {
                new GameView.GameFinish();
            }
            if (timer != null) {
                timer.stop();
            }
            // Partial reset: keep score, time, and moves
            view.playMode.setEnabled(true);
            view.dimComoBox.setEnabled(true);
            view.showButton.setEnabled(true);
            view.hideButton.setEnabled(true);
            view.typeChoice.setEnabled(true);
            view.designButton.setEnabled(true);
        }
    }

    /**
     * Reset the game
     */
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

    /**
     * Listens for the "New" menu item being selected
     * It resets the game and scrambles the board.
     */
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

    /**
     * Listens for the reset button being clicked
     * Resets the game
     */
    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();
        }
    }

    /**
     * Listens for the "Colors" menu item being clicked
     * Shows the color selection menu
     */
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

    /**
     * Listens for the "Solution" menu item being clicked
     * Shows the game solution
     */
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

    /**
     * Listens for the "Show" button being clicked
     * Shows the game solution, but only in "Design" mode
     */
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
                    board[i][j].setBackground(Color.WHITE);
                }
            }
            model.setBoard(board);
        }
    }

    /**
     * Listens for the "Hide" button being clicked
     * Scrambles the board, but only in "Design" mode
     */
    private class HideButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton[][] board = model.getBoard();
            int dim = model.getDim();
            Random rand = new Random();
            // Randomize the positions of the tiles
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    int randomRow = rand.nextInt(dim);
                    int randomColumn = rand.nextInt(dim);
                    String temp = board[i][j].getText();
                    board[i][j].setText(board[randomRow][randomColumn].getText());
                    board[randomRow][randomColumn].setText(temp);
                    board[i][j].setBackground(Color.WHITE);
                    board[i][j].setEnabled(true);
                }
            }
            // Re-style and disable the empty button
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (board[i][j].getText().equals("")) {
                        board[i][j].setBackground(Color.BLACK);
                        board[i][j].setEnabled(false);
                    }
                }
            }
            model.setBoard(board);
        }
    }

    /**
     * Listens for the "Save" button being clicked
     * Saves the solution to a file
     */
    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            String[][] solution = model.getSolution();
            int dim = model.getDim();
            fileChooser.addActionListener(e1 -> {
                File file = fileChooser.getSelectedFile();
                if (file == null) {
                    return;
                }
                try (FileWriter writer = new FileWriter(file)) {
                    StringBuilder output = new StringBuilder();
                    String isNumber = "false";
                    if (view.typeChoice.getSelectedIndex() == 0) {
                        isNumber = "true";
                    }
                    output.append(dim).append("\n").append(isNumber).append("\n");
                    for (int i = 0; i < dim; i++) {
                        for (int j = 0; j < dim; j++) {
                            if (i == (dim - 1) && j == (dim - 1)) {
                                output.append(solution[i][j]);
                                continue;
                            }
                            output.append(solution[i][j]).append("\n");
                        }
                    }
                    writer.write(output.toString());
                } catch (IOException | NullPointerException error) {
                    error.printStackTrace();
                }
            });
            fileChooser.showOpenDialog(view);
        }
    }

    /**
     * Listens for the "Load" button being clicked
     * Loads a solution from a file
     */
    private class LoadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addActionListener(e1 -> {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    JButton[][] board = model.getBoard();
                    int dim = Integer.parseInt(reader.readLine());
                    String isNumber = reader.readLine();
                    String line;
                    // Read every line in the file, a solution always ends with "null"
                    ArrayList<String> lines = new ArrayList<>();
                    while (!((line = reader.readLine()).equals("null"))) {
                        lines.add(line);
                    }
                    int linesIndex = 0;
                    for (int i = 0; i < dim; i++) {
                        for (int j = 0; j < dim; j++) {
                            if (linesIndex == lines.size()) {
                                board[i][j].setText("");
                                board[i][j].setBackground(Color.BLACK);
                                board[i][j].setEnabled(false);
                                break;
                            }
                            board[i][j].setText(lines.get(linesIndex));
                            board[i][j].setBackground(Color.WHITE);
                            board[i][j].setEnabled(true);
                            linesIndex++;
                        }
                    }
                    model.setBoard(board);
                } catch (IOException error) {
                    error.printStackTrace();
                }
            });
            fileChooser.showOpenDialog(view);
        }
    }

    /**
     * Listens for the "Exit" menu item being clicked
     * Exits the application
     */
    private class ExitMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
