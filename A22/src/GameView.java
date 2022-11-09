import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends JFrame {
    /**
     * The layout for the whole application
     */
    public BorderLayout layout;
    /**
     * The layout for the gameplay part of the application
     */
    public GridLayout gridLayout;
    /**
     * A text area for displaying info about the game execution
     */
    public JTextArea sideInfo;
    public JPanel mainGamePanel;
    public JLabel movesCountLabel;
    public JLabel timeElapsed;
    public JRadioButton designButton;
    public JRadioButton playMode;
    public JButton showButton;
    public JButton hideButton;
    public JComboBox typeChoice;
    public TextField designText;
    public JButton setDesignButton;
    public JLabel pointsCountLabel;
    public JComboBox dimComoBox;
    public JButton finishButton;
    public JMenuItem newMenuItem;
    public JMenuItem colorsMenuItem;
    public JButton resetButton;
    public JButton saveButton;
    JButton loadButton;
    public JMenuItem solutionMenuItem;
    public JMenuItem exitMenuItem;
    Random rand = new Random();

    public GameView() {
        super("NumPuz");
    }

    public JButton[][] initializeView(int initialDim, ActionListener dimBoxListener, String solution, GameModel model) {
        GameSplash splash = new GameSplash();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new BorderLayout();
        gridLayout = new GridLayout(initialDim, initialDim);
        setSize(1200, 600);
        setLayout(layout);

        // Border layout for main image and design text
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Main image
        Icon logo = new ImageIcon(getClass().getResource("gamelogo.png"));
        JLabel logoLabel = new JLabel(logo);
        topPanel.add(logoLabel, BorderLayout.NORTH);

        // Design text input
        designText = new TextField(15);
        topPanel.add(designText, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Set design text button
        setDesignButton = new JButton("Set");
        topPanel.add(setDesignButton, BorderLayout.EAST);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        newMenuItem = new JMenuItem("New");
        solutionMenuItem = new JMenuItem("Solution");
        exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(newMenuItem);
        gameMenu.add(solutionMenuItem);
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);

        JMenu helpMenu = new JMenu("Help");
        colorsMenuItem = new JMenuItem("Colors");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(colorsMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Main game area
        mainGamePanel = new JPanel(gridLayout);
        add(mainGamePanel, BorderLayout.CENTER);

        JButton[][] board = setupBoard(initialDim, solution, model);

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
        designButton = new JRadioButton("Design");
        playMode = new JRadioButton("Play");
        designButton.setSelected(true);
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
        timeElapsed = new JLabel("0");
        timePanelConstraints.gridx = 0;
        timePanelConstraints.gridy = 0;
        timePanel.add(timeElapsedLabel, timePanelConstraints);
        timePanelConstraints.gridx = 0;
        timePanelConstraints.gridy = 1;
        timePanel.add(timeElapsed, timePanelConstraints);

        // Dimension Selection
        JLabel dimLabel = new JLabel("Dim");
        dimComoBox = new JComboBox(new String[]{"3", "4", "5"});
        dimComoBox.setSelectedIndex(0);
        dimSelectionConstraints.gridx = 0;
        dimSelectionConstraints.gridy = 0;
        dimSelectionPanel.add(dimLabel, dimSelectionConstraints);
        dimSelectionConstraints.gridx = 0;
        dimSelectionConstraints.gridy = 1;
        dimSelectionPanel.add(dimComoBox, dimSelectionConstraints);

        // Solution
        JLabel solutionLabel = new JLabel("Solution");
        showButton = new JButton("Show");
        hideButton = new JButton("Hide");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 0;
        solutionConstraints.gridwidth = 4;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(solutionLabel, solutionConstraints);
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 1;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(showButton, solutionConstraints);
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 2;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(hideButton, solutionConstraints);
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 3;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(saveButton, solutionConstraints);
        solutionConstraints.gridx = 0;
        solutionConstraints.gridy = 4;
        solutionConstraints.gridwidth = 1;
        solutionConstraints.gridheight = 1;
        solutionPanel.add(loadButton, solutionConstraints);

        // Type
        JLabel typeLabel = new JLabel("Type");
        typeChoice = new JComboBox(new String[]{"Number", "Text"});
        typeChoice.setSelectedIndex(0);
        designText.setEnabled(false);
        setDesignButton.setEnabled(false);
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
        pointsCountLabel = new JLabel("0");
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

        // Finish button
        finishButton = new JButton("Finish");
        bottomPanel.add(finishButton);

        // Reset button
        resetButton = new JButton("Reset");
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
//        layout.layoutContainer(getContentPane());
        setVisible(true);
        dimComoBox.addActionListener(dimBoxListener);

        return board;
    }

    public JButton[][] setupBoard(int dim, String solution, GameModel model) {
        // Game buttons
        JButton[][] board = new JButton[dim][dim];
        gridLayout.setColumns(dim);
        gridLayout.setRows(dim);
        mainGamePanel.removeAll();

        String[] solutionSplit = solution.split("_");
        System.out.println(solution);

        ArrayList<String> tilesToAdd = new ArrayList<>();
        String[][] outSolution = new String[dim][dim];
        for (int i = 0; i < solutionSplit.length; i++) {
            tilesToAdd.add(solutionSplit[i]);
        }

        // there should be dim*dim - 1 buttons, 1 space is left empty
        int solutionIndex = 0;
        int tileNumber = 1;
        int tilesAdded = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tilesAdded == (dim * dim - 1)) {
                    board[i][j] = new JButton();
                    board[i][j].setEnabled(false);
                    board[i][j].setBackground(Color.BLACK);
                    board[i][j].setFont(new Font ("Verdana", Font.PLAIN, 70));
                    mainGamePanel.add(board[i][j]);
                    break;
                }
                outSolution[i][j] = solutionSplit[solutionIndex];
                String randomTile = tilesToAdd.get(rand.nextInt(tilesToAdd.size()));
                tilesToAdd.remove(randomTile);
                board[i][j] = new JButton(randomTile);
                board[i][j].setBackground(Color.WHITE);
                board[i][j].setFont(new Font ("Verdana", Font.PLAIN, 70));
                mainGamePanel.add(board[i][j]);
                tileNumber++;
                tilesAdded++;
                solutionIndex++;
            }
        }
        model.setSolution(outSolution);
        return board;
    }

    private class GameSplash extends JWindow {
        public GameSplash() {
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Color.BLACK);
            int width = 800;
            int height = 600;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            setBounds(x, y, width, height);
            JLabel label = new JLabel(new ImageIcon(getClass().getResource("game-splash.png")));
            content.add(label, BorderLayout.CENTER);
            setContentPane(content);
            setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispose();
        }
    }

    public static class GameWinner extends JWindow {
        public GameWinner() {
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Color.BLACK);
            int width = 800;
            int height = 600;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            setBounds(x, y, width, height);
            JLabel label = new JLabel(new ImageIcon(getClass().getResource("gamewinner.png")));
            content.add(label, BorderLayout.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> dispose());
            content.add(okButton, BorderLayout.SOUTH);
            setContentPane(content);
            setVisible(true);
        }
    }

    public static class GameFinish extends JWindow {
        public GameFinish() {
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Color.BLACK);
            int width = 800;
            int height = 600;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            setBounds(x, y, width, height);
            JLabel label = new JLabel(new ImageIcon(getClass().getResource("gameend.png")));
            content.add(label, BorderLayout.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> dispose());
            content.add(okButton, BorderLayout.SOUTH);
            setContentPane(content);
            setVisible(true);
        }
    }

    public static class ColorChanger extends JFrame {
        public JButton setColor1Button;
        public JButton setColor2Button;
        public JButton color1Button;
        public JButton color2Button;

        public ColorChanger(Color color1, Color color2) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            GridLayout colorLayout = new GridLayout();
            colorLayout.setColumns(2);
            colorLayout.setRows(2);

            setSize(800, 600);

            JPanel content = new JPanel(colorLayout);
            color1Button = new JButton();
            color1Button.setBackground(color1);
            color2Button = new JButton();
            color2Button.setBackground(color2);

            setColor1Button = new JButton("Color1");
            setColor1Button.addActionListener(e -> {

            });
            setColor2Button = new JButton("Color2");

            setLayout(colorLayout);
            content.add(color1Button);
            content.add(color2Button);
            content.add(setColor1Button);
            content.add(setColor2Button);

            setContentPane(content);
            setVisible(true);
        }

        public void setButton1Color(Color color) {
            color1Button.setBackground(color);
        }

        public void setButton2Color(Color color) {
            color2Button.setBackground(color);
        }
    }

    public static class ColorPicker extends JFrame {
        JColorChooser colorPicker;

        public ColorPicker(Color selected, GameModel model) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel panel = new JPanel(new BorderLayout());
            setSize(800, 600);
            colorPicker = new JColorChooser(selected);
            JButton okButton = new JButton("OK");
            // Close the window, same as if we pressed the X button
            okButton.addActionListener(e -> {
                dispose();
            });
            panel.add(colorPicker, BorderLayout.CENTER);
            panel.add(okButton, BorderLayout.SOUTH);
            setContentPane(panel);
            setVisible(true);
        }
    }
}
