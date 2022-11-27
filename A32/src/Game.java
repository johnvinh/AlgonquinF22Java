/*
Name: John Vinh
Course: CST8221
Assignment: A22
Professor: Paulo Sousa
 */

/**
 * The main class, runs the game
 */
public class Game {
    /**
     * Run the game.
     * @param args  arguments to be sent to the application
     */
    public static void main(String[] args) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();
        GameController main = new GameController(gameModel, gameView);
        NumPuzServerGui serverGui = new NumPuzServerGui();
    }
}
