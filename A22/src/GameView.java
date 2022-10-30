import javax.swing.*;
import java.awt.*;

public class GameView {
    public GameView() {
        GameSplash splash = new GameSplash();
    }

    private class GameSplash extends JWindow {
        public GameSplash() {
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Color.BLACK);
            setBounds(400, 200, 800, 600);
            JLabel label = new JLabel(new ImageIcon(getClass().getResource("game.png")));
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
}
