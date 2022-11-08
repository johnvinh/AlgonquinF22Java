/*
Name: John Vinh
Course: CST8221
Assignment: A12
Professor: Paulo Sousa
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController extends JFrame {
    private GameModel model;
    private GameView view;

    GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    public int getDim() {
        return model.getDim();
    }

    public void initialize() {
        view.initializeView(model.getDim(), new DimBoxListener());
        view.playMode.addActionListener(new EnableTimeIncrementer());
    }

    private class DimBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            int newDim = Integer.parseInt(String.valueOf(source.getSelectedItem()));
            model.setDim(newDim);

            // there should be dim*dim - 1 buttons, 1 space is left empty
            model.setBoard(view.setupBoard(model.getDim()));

            view.mainGamePanel.revalidate();
            view.mainGamePanel.repaint();
        }
    }

    private class EnableTimeIncrementer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new Timer(1000, new TimeIncrementer()).start();
        }
    }

    private class TimeIncrementer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int time = Integer.parseInt(view.timeElapsed.getText());
            time++;
            view.timeElapsed.setText(String.valueOf(time));
        }
    }
}
