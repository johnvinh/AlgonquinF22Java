/*
Name: John Vinh
Course: CST8221
Assignment: A12
Professor: Paulo Sousa
 */

import javax.swing.*;
import java.awt.*;

public class GameController extends JFrame {
    private final BorderLayout layout;
    private final GridLayout gridLayout;
    private final JButton[] gameButtons;
    private int dim = 0;

    public static void main(String[] args) {
        GameController main = new GameController();
    }

    GameController() {
        super("NumPuz");
        dim = 3;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new BorderLayout();
        gridLayout = new GridLayout(dim, dim);
        setSize(800, 600);
        setLayout(layout);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        // Main game area
        JPanel mainGamePanel = new JPanel(gridLayout);
        add(mainGamePanel, BorderLayout.WEST);

        // Game buttons
        gameButtons = new JButton[dim * dim];
        // there should be dim*dim - 1 buttons, 1 space is left empty
        for (int i = 0; i < (dim * dim) - 1; i++) {
            gameButtons[i] = new JButton(String.format("%s", (i + 1)));
            mainGamePanel.add(gameButtons[i]);
        }

        setVisible(true);
    }

    private class Controller {

    }
}
