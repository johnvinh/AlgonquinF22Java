import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class NumPuzServerGui extends JFrame {
    private final JLabel portLabel;
    private final JTextField portInput;
    private final JButton startButton;
    private final JButton resultsButton;
    private final JButton endButton;
    private final JTextArea logTextArea;
    NumPuzServer server;
    NumPuzServerGui serverGui;
    private String configuration;

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

        serverGui = this;
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getConfiguration() {
        return configuration;
    }

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

    private class ResultsButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
