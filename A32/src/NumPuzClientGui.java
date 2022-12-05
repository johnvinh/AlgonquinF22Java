import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

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
    private Socket client;
    private String solution;
    private GameController controller;
    int clientId;

    NumPuzClientGui() {
        super("Game Client");
        // Resize the image: https://stackoverflow.com/a/18335435
        ImageIcon logoImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("client.png")));
        Image image = logoImage.getImage();
        Image newImage = image.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        Icon logo = new ImageIcon(newImage);
        JLabel logoLabel = new JLabel(logo);

        JPanel logoPanel = new JPanel();
        logoPanel.add(logoLabel);

        setLayout(new BorderLayout());

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
        logArea = new JTextArea(20, 40);

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
        newGameButton.setEnabled(false);
        sendGameButton.setEnabled(false);
        receiveGameButton.setEnabled(false);
        sendDataButton.setEnabled(false);
        playButton.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logArea);

        JPanel doubleMiddlePanel = new JPanel();
        doubleMiddlePanel.setLayout(new BorderLayout());
        doubleMiddlePanel.add(topPanel, BorderLayout.NORTH);
        doubleMiddlePanel.add(middlePanel, BorderLayout.CENTER);

        add(logoPanel, BorderLayout.NORTH);
        add(doubleMiddlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(600, 600);

        setVisible(true);

        connectButton.addActionListener(new ConnectButtonClick());
        newGameButton.addActionListener(new NewGameButtonClick());
        sendGameButton.addActionListener(new SendGameButtonClick());
        receiveGameButton.addActionListener(new ReceiveGameButtonClick());
        playButton.addActionListener(new PlayButtonClick());
        sendDataButton.addActionListener(new SendDataButtonClick());
    }

    private class ConnectButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredServer = server.getText();
            String enteredPort = port.getText();
            int port;

            try {
                port = Integer.parseInt(enteredPort);
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(null, "That port is invalid.",
                        "Invalid Port", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                client = new Socket(enteredServer, port);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error connecting to server!",
                        "Server Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println(user.getText());

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            logArea.append("Connected to server!\n");
            newGameButton.setEnabled(true);
            sendGameButton.setEnabled(true);
            receiveGameButton.setEnabled(true);
            playButton.setEnabled(true);
            sendDataButton.setEnabled(true);

            Thread thread = new Thread(new GetClientId());
            thread.start();
        }
    }

    private class NewGameButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            solution = GameController.dimToSolution(3);
            logArea.append("New game configuration: " + solution + "\n");
        }
    }

    private class SendGameButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println("config:" + solution);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            logArea.append("Sent game configuration to server\n");
        }
    }

    private class ReceiveGameButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Thread thread = new Thread(new ReceiveGameData());
            thread.start();
        }
    }

    private class ReceiveGameData implements Runnable {

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println("requestConfig");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = in.readLine();
                if (message.startsWith("config:")) {
                    solution = message.split(":")[1];
                    logArea.append("Received config from server: " + solution + "\n");
                }
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }
    }

    private class PlayButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (solution == null) {
                JOptionPane.showMessageDialog(null,
                        "Missing configuration. Create one or receive one from the server",
                        "Missing Configuration", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GameModel gameModel = new GameModel();
            GameView gameView = new GameView(user.getText() + "'s NumPuz Game");
            controller = new GameController(gameModel, gameView, solution);
        }
    }

    private class SendDataButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (controller == null) {
                JOptionPane.showMessageDialog(null, "Game not started!",
                        "Game Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GameModel model = controller.getModel();
            int moves = model.getMoves();
            int score = model.getScore();
            int time = model.getTimeElapsed();

            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println("data:" + clientId + "," + moves + "," + score + "," + time);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            logArea.append(String.format("Sent game data to server: %d moves, %d score, %d time\n", moves, score, time));
        }
    }

    private class GetClientId implements Runnable {

        @Override
        public void run() {
            System.out.println("Started listening for a message");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = in.readLine();
                if (message.startsWith("id:")) {
                    clientId = Integer.parseInt(message.split(":")[1]);
                    logArea.append("Received client ID from server: " + clientId + "\n");
                }
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }
    }
}
