import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NumPuzServerGui extends JFrame {
    private final JLabel portLabel;
    private final JTextField portInput;
    private final JButton startButton;
    private final JButton endButton;
    private final JTextArea logTextArea;
    NumPuzServer server;
    NumPuzServerGui serverGui;
    private String configuration;

    NumPuzServerGui() {
        super("Game Server");
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        setSize(800, 600);

        portLabel = new JLabel("Port:");
        portInput = new JTextField(8);
        startButton = new JButton("Start");
        endButton = new JButton("End");
        logTextArea = new JTextArea(40, 40);

        JPanel middlePanel = new JPanel();
        middlePanel.add(portLabel);
        middlePanel.add(portInput);
        middlePanel.add(startButton);
        middlePanel.add(endButton);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logTextArea);

        add(middlePanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
        setVisible(true);

        startButton.addActionListener(new StartButtonClick());

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

            logTextArea.append("Successfully started server!\n");
        }
    }
}
