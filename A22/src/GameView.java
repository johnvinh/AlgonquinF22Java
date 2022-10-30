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
            int width = 800;
            int height = 600;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            setBounds(x, y, width, height);
            JLabel label = new JLabel(new ImageIcon(getClass().getResource("game.png")));
            content.add(label, BorderLayout.CENTER);
            setContentPane(content);
            setVisible(true);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispose();
        }
    }
}
