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
    private final JTextArea sideInfo;

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
        add(mainGamePanel, BorderLayout.CENTER);

        // Game buttons
        gameButtons = new JButton[dim * dim];
        // there should be dim*dim - 1 buttons, 1 space is left empty
        for (int i = 0; i < (dim * dim) - 1; i++) {
            gameButtons[i] = new JButton(String.format("%s", (i + 1)));
            mainGamePanel.add(gameButtons[i]);
        }

        // Side info
        JPanel sidePanel = new JPanel();
        sideInfo = new JTextArea(100, 10);
        sidePanel.add(sideInfo);
        add(sidePanel, BorderLayout.EAST);

        // Bottom info
        JPanel modePanel = new JPanel(new GridBagLayout());
        JPanel timePanel = new JPanel();
        JPanel dimSelectionPanel = new JPanel();
        JPanel solutionPanel = new JPanel();
        JPanel typePanel = new JPanel();
        JPanel bottomPanel = new JPanel(new GridLayout(2, 5));

        // Mode
        JLabel modeLabel = new JLabel("Mode");
        JRadioButton designButton = new JRadioButton("Design");
        JRadioButton playMode = new JRadioButton("Play");
        modePanel.add(modeLabel);
        modePanel.add(designButton);
        modePanel.add(playMode);

        // Time
        JLabel timeElapsedLabel = new JLabel("Time Elapsed");
        JLabel timeElapsed = new JLabel("0:00");
        timePanel.add(timeElapsedLabel);
        timePanel.add(timeElapsed);

        // Dimension Selection
        JLabel dimLabel = new JLabel("Dim");
        JComboBox dimComoBox = new JComboBox(new String[]{"3", "4", "5"});
        dimComoBox.setSelectedIndex(0);
        dimSelectionPanel.add(dimLabel);
        dimSelectionPanel.add(dimComoBox);

        // Solution
        JLabel solutionLabel = new JLabel("Solution");
        JButton showButton = new JButton("Show");
        JButton hideButton = new JButton("Hide");
        solutionPanel.add(solutionLabel);
        solutionPanel.add(showButton);
        solutionPanel.add(hideButton);

        // Type
        JLabel typeLabel = new JLabel("Type");
        JComboBox typeChoice = new JComboBox(new String[]{"Number", "Text"});
        typeChoice.setSelectedIndex(0);
        typePanel.add(typeLabel);
        typePanel.add(typeChoice);


        bottomPanel.add(modePanel);
        bottomPanel.add(timePanel);
        bottomPanel.add(dimSelectionPanel);
        bottomPanel.add(solutionPanel);
        bottomPanel.add(typePanel);

        // Reset button
        JButton resetButton = new JButton("Reset");
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
//        layout.layoutContainer(getContentPane());
        setVisible(true);
    }

    private class Controller {

    }
}
