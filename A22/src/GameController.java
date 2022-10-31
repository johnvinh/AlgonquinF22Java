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
    /**
     * The layout for the whole application
     */
    private final BorderLayout layout;
    /**
     * The layout for the gameplay part of the application
     */
    private final GridLayout gridLayout;
    /**
     * A text area for displaying info about the game execution
     */
    private final JTextArea sideInfo;
    private GameModel model;
    private GameView view;
    private JPanel mainGamePanel;
    private int moves = 0;
    JLabel movesCountLabel;

    GameController(GameModel model, GameView view) {
        super("NumPuz");
        model.setDim(3);
        this.model = model;
        this.view = view;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new BorderLayout();
        gridLayout = new GridLayout(model.getDim(), model.getDim());
        setSize(1200, 600);
        setLayout(layout);

        // Main image
        Icon logo = new ImageIcon(getClass().getResource("gamelogo.png"));
        JLabel logoLabel = new JLabel(logo);
        add(logoLabel, BorderLayout.NORTH);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        // Main game area
        mainGamePanel = new JPanel(gridLayout);
        add(mainGamePanel, BorderLayout.CENTER);

        setupBoard();

        // Side info
        JPanel sidePanel = new JPanel();
        sideInfo = new JTextArea(100, 10);
        sidePanel.add(sideInfo);
        add(sidePanel, BorderLayout.EAST);

        // Bottom info
        JPanel modePanel = new JPanel();
        GridBagLayout modePanelLayout = new GridBagLayout();
        modePanel.setLayout(modePanelLayout);
        GridBagConstraints modePanelConstraints = new GridBagConstraints();

        JPanel timePanel = new JPanel();
        GridBagLayout timePanelLayout = new GridBagLayout();
        timePanel.setLayout(timePanelLayout);
        GridBagConstraints timePanelConstraints = new GridBagConstraints();

        JPanel dimSelectionPanel = new JPanel();
        GridBagLayout dimSelectionLayout = new GridBagLayout();
        dimSelectionPanel.setLayout(dimSelectionLayout);
        GridBagConstraints dimSelectionConstraints = new GridBagConstraints();

        JPanel solutionPanel = new JPanel();
        GridBagLayout solutionLayout = new GridBagLayout();
        solutionPanel.setLayout(solutionLayout);
        GridBagConstraints solutionConstraints = new GridBagConstraints();

        JPanel typePanel = new JPanel();
        GridBagLayout typeLayout = new GridBagLayout();
        typePanel.setLayout(typeLayout);
        GridBagConstraints typeConstraints = new GridBagConstraints();

        JPanel movesPanel = new JPanel();
        GridBagLayout movesLayout = new GridBagLayout();
        movesPanel.setLayout(movesLayout);
        GridBagConstraints movesConstraints = new GridBagConstraints();

        JPanel pointsPanel = new JPanel();
        GridBagLayout pointsLayout = new GridBagLayout();
        pointsPanel.setLayout(pointsLayout);
        GridBagConstraints pointsConstraints = new GridBagConstraints();

        JPanel bottomPanel = new JPanel(new GridLayout(1, 8, 0, 0));

        // Mode
        JLabel modeLabel = new JLabel("Mode");
        JRadioButton designButton = new JRadioButton("Design");
        JRadioButton playMode = new JRadioButton("Play");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(designButton);
        modeGroup.add(playMode);
        modePanelConstraints.fill = GridBagConstraints.VERTICAL;
        modePanelConstraints.gridx = 0;
        modePanelConstraints.gridy = 0;
        modePanelConstraints.gridwidth = 2;
        modePanelConstraints.gridheight = 1;
        modePanel.add(modeLabel, modePanelConstraints);
        modePanel.add(designButton);
        modePanelConstraints.gridx = 0;
        modePanelConstraints.gridy = 1;
        modePanelConstraints.gridwidth = 1;
        modePanelConstraints.gridheight = 1;
        modePanel.add(designButton, modePanelConstraints);
        modePanelConstraints.gridx = 1;
        modePanelConstraints.gridy = 1;
        modePanelConstraints.gridwidth = 1;
        modePanelConstraints.gridheight = 1;
        modePanel.add(playMode, modePanelConstraints);

        // Time
        JLabel timeElapsedLabel = new JLabel("Time Elapsed");
        JLabel timeElapsed = new JLabel("0");
        timePanelConstraints.gridx = 0;
        timePanelConstraints.gridy = 0;
        timePanel.add(timeElapsedLabel, timePanelConstraints);
        timePanelConstraints.gridx = 0;
        timePanelConstraints.gridy = 1;
        timePanel.add(timeElapsed, timePanelConstraints);
        ActionListener timeIncrementer = e -> {
            int time = Integer.parseInt(timeElapsed.getText());
            time++;
            timeElapsed.setText(String.valueOf(time));
        };
        new Timer(1000, timeIncrementer).start();

        // Dimension Selection
        JLabel dimLabel = new JLabel("Dim");
        JComboBox dimComoBox = new JComboBox(new String[]{"3", "4", "5"});
        dimComoBox.setSelectedIndex(0);
        dimSelectionConstraints.gridx = 0;
        dimSelectionConstraints.gridy = 0;
        dimSelectionPanel.add(dimLabel, dimSelectionConstraints);
        dimSelectionConstraints.gridx = 0;
        dimSelectionConstraints.gridy = 1;
        dimSelectionPanel.add(dimComoBox, dimSelectionConstraints);

        // Solution
        JLabel solutionLabel = new JLabel("Solution");
        JButton showButton = new JButton("Show");
        JButton hideButton = new JButton("Hide");
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 0;
        solutionConstraints.gridwidth = 2;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(solutionLabel, solutionConstraints);
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 1;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(showButton, solutionConstraints);
        solutionConstraints.gridx = 1;
        solutionConstraints.gridy = 1;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(hideButton, solutionConstraints);

        // Type
        JLabel typeLabel = new JLabel("Type");
        JComboBox typeChoice = new JComboBox(new String[]{"Number", "Text"});
        typeChoice.setSelectedIndex(0);
        typeConstraints.gridx = 0;
        typeConstraints.gridy = 0;
        typePanel.add(typeLabel, typeConstraints);
        typeConstraints.gridx = 0;
        typeConstraints.gridy = 1;
        typePanel.add(typeChoice, typeConstraints);

        // Moves
        JLabel movesLabel = new JLabel("Moves:");
        movesCountLabel = new JLabel("0");
        movesConstraints.gridx = 0;
        movesConstraints.gridy = 0;
        movesPanel.add(movesLabel, movesConstraints);
        movesConstraints.gridx = 0;
        movesConstraints.gridy = 1;
        movesPanel.add(movesCountLabel, movesConstraints);

        // Points
        JLabel pointsLabel = new JLabel("Points:");
        JLabel pointsCountLabel = new JLabel("0");
        pointsConstraints.gridx = 0;
        pointsConstraints.gridy = 0;
        pointsPanel.add(pointsLabel, pointsConstraints);
        pointsConstraints.gridx = 0;
        pointsConstraints.gridy = 1;
        pointsPanel.add(pointsCountLabel, pointsConstraints);

        bottomPanel.add(modePanel);
        bottomPanel.add(timePanel);
        bottomPanel.add(dimSelectionPanel);
        bottomPanel.add(solutionPanel);
        bottomPanel.add(typePanel);
        bottomPanel.add(movesPanel);
        bottomPanel.add(pointsPanel);

        // Reset button
        JButton resetButton = new JButton("Reset");
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
//        layout.layoutContainer(getContentPane());
        setVisible(true);
        dimComoBox.addActionListener(new DimBoxListener());
    }

    private void setupBoard() {
        // Game buttons
        JButton[][] board = new JButton[model.getDim()][model.getDim()];
        int newDim = model.getDim();
        gridLayout.setColumns(newDim);
        gridLayout.setRows(newDim);
        mainGamePanel.removeAll();

        // there should be dim*dim - 1 buttons, 1 space is left empty
        int tileNumber = 1;
        int tilesAdded = 0;
        for (int i = 0; i < model.getDim(); i++) {
            for (int j = 0; j < model.getDim(); j++) {
                if (tilesAdded == (model.getDim() * model.getDim() - 1)) {
                    break;
                }
                board[i][j] = new JButton(String.format("%s", tileNumber));
                board[i][j].addActionListener(e -> {
                    moves++;
                    movesCountLabel.setText(String.valueOf(moves));
                });
                mainGamePanel.add(board[i][j]);
                tileNumber++;
                tilesAdded++;
            }
        }
        model.setBoard(board);
    }

    private class DimBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            int newDim = Integer.parseInt(String.valueOf(source.getSelectedItem()));
            model.setDim(newDim);

            // there should be dim*dim - 1 buttons, 1 space is left empty
            setupBoard();

            mainGamePanel.revalidate();
            mainGamePanel.repaint();
        }
    }
}
