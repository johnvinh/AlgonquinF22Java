/*
Name: John Vinh
Course: CST8221
Assignment: A12
Professor: Paulo Sousa
 */

import javax.swing.*;
import java.awt.*;

public class GameController extends JFrame {
    private BorderLayout layout;

    public static void main(String[] args) {
        GameController main = new GameController();
    }

    GameController() {
        super("NumPuz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new BorderLayout();
        setSize(800, 600);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
        setVisible(true);
    }

    private class Controller {

    }
}
