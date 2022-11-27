import javax.swing.*;
import java.awt.*;

public class NumPuzServerGui extends JFrame {
    private final JLabel portLabel;
    private final JTextField portInput;
    private final JButton startButton;
    private final JButton endButton;
    private final JTextArea logTextArea;

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
    }
}
