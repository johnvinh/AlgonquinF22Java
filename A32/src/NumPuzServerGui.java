import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A NumPuz server GUI.
 */
public class NumPuzServerGui extends JFrame {
    /**
     * A label for the port text field.
     */
    private final JLabel portLabel;
    /**
     * The port to listen on.
     */
    private final JTextField portInput;
    /**
     * Starts the server when clicked.
     */
    private final JButton startButton;
    /**
     * Shows the results of the game when clicked.
     */
    private final JButton resultsButton;
    /**
     * Ends the server when clicked.
     */
    private final JButton endButton;
    /**
     * Logs server connection info.
     */
    private final JTextArea logTextArea;
    /**
     * An instance of the server.
     */
    NumPuzServer server;
    /**
     * The current instance of this class to be used in the start server button.
     */
    NumPuzServerGui serverGui;
    /**
     * A solution to the current game configuration.
     */
    private String configuration;

    /**
     * Creates a new server GUI.
     */
    NumPuzServerGui() {
        super("Game Server");
        // Main image
        // Resize the image: https://stackoverflow.com/a/18335435
        ImageIcon logoImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("server.png")));
        Image image = logoImage.getImage();
        Image newImage = image.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        Icon logo = new ImageIcon(newImage);
        JLabel logoLabel = new JLabel(logo);

        JPanel topPanel = new JPanel();
        topPanel.add(logoLabel);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        add(topPanel, BorderLayout.NORTH);
        setSize(600, 600);

        portLabel = new JLabel("Port:");
        portInput = new JTextField(8);
        startButton = new JButton("Start");
        resultsButton = new JButton("Results");
        resultsButton.setEnabled(false);
        endButton = new JButton("End");
        logTextArea = new JTextArea(20, 40);

        JPanel middlePanel = new JPanel();
        middlePanel.add(portLabel);
        middlePanel.add(portInput);
        middlePanel.add(startButton);
        middlePanel.add(resultsButton);
        middlePanel.add(endButton);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logTextArea);

        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);

        startButton.addActionListener(new StartButtonClick());
        resultsButton.addActionListener(new ResultsButtonClick());
        endButton.addActionListener(new EndButtonClick());

        serverGui = this;
    }

    /**
     * Gets the log text area.
     * @return  the log text area of this GUI
     */
    public JTextArea getLogTextArea() {
        return logTextArea;
    }

    /**
     * Sets the configuration.
     * @param configuration the game configuration
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets the game configuration.
     * @return  the game configuration
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Starts the game.
     */
    private class StartButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredPort = portInput.getText();
            int port;
            try {
                port = Integer.parseInt(enteredPort);
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(null, "That port number is invalid.",
                        "Invalid Port Number", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                server = new NumPuzServer(port, serverGui);
            } catch (IOException error) {
                JOptionPane.showMessageDialog(null, "Error starting the server!",
                        "Error Starting Server", JOptionPane.ERROR_MESSAGE);
                return;
            }

            resultsButton.setEnabled(true);
            logTextArea.append("Successfully started server!\n");
        }
    }

    /**
     * Shows the game results of each client.
     */
    private class ResultsButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HashMap<Integer, ArrayList<Integer>> playerStats = server.getPlayerStats();
            HashMap<Integer, String> playerNames = server.getPlayerNames();
            StringBuilder outputMessage = new StringBuilder();
            outputMessage.append("Game results:\n");
            for (Integer key : playerStats.keySet()) {
                ArrayList<Integer> stats = playerStats.get(key);
                outputMessage.append("Player[").append(key).append("]: ");
                outputMessage.append(playerNames.get(key));
                outputMessage.append(", points: ").append(stats.get(1));
                outputMessage.append(", time: ").append(stats.get(2));
                outputMessage.append(", moves: ").append(stats.get(0));
                outputMessage.append("\n");
            }
            JOptionPane.showMessageDialog(null, outputMessage.toString(),
                    "Game Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Ends the server.
     */
    private class EndButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            server.close();
            logTextArea.append("Server closed\n");
        }
    }
}
