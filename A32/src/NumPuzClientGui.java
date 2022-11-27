import javax.swing.*;
import java.awt.*;

public class NumPuzClientGui extends JFrame {
    private final JLabel userLabel;
    private final JTextField user;
    private final JLabel serverLabel;
    private final JTextField server;
    private final JLabel portLabel;
    private final JTextField port;
    private final JButton connectButton;
    private final JButton endButton;
    private final JButton newGameButton;
    private final JButton sendGameButton;
    private final JButton receiveGameButton;
    private final JButton sendDataButton;
    private final JButton playButton;
    private final JTextArea logArea;

    NumPuzClientGui() {
        super("Game Client");
        setLayout(new BorderLayout());
        setSize(800, 600);
        userLabel = new JLabel("User:");
        user = new JTextField(8);
        serverLabel = new JLabel("Server:");
        server = new JTextField(8);
        portLabel = new JLabel("Port:");
        port = new JTextField(8);
        connectButton = new JButton("Connect");
        endButton = new JButton("End");
        newGameButton = new JButton("New game");
        sendGameButton = new JButton("Send game");
        receiveGameButton = new JButton("Receive game");
        sendDataButton = new JButton("Send data");
        playButton = new JButton("Play");
        logArea = new JTextArea(20, 20);

        JPanel topPanel = new JPanel();
        topPanel.add(userLabel);
        topPanel.add(user);
        topPanel.add(serverLabel);
        topPanel.add(server);
        topPanel.add(portLabel);
        topPanel.add(port);
        topPanel.add(connectButton);
        topPanel.add(endButton);

        JPanel middlePanel = new JPanel();
        middlePanel.add(newGameButton);
        middlePanel.add(sendGameButton);
        middlePanel.add(receiveGameButton);
        middlePanel.add(sendDataButton);
        middlePanel.add(playButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logArea);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
