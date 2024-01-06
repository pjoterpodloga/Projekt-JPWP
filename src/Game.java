package src;

import javax.swing.*;

public class Game extends JFrame{

    // Window constants //
    public static final int WIDTH = 1280;   // Width of window
    public static final int HEIGHT = 1024;  // Height of window
    public static final String NAME = "Szalone wykresy TEST"; // Title of window

    private static Game Instance; // Instance of game
    protected static int lastID = 1; // Last ID of instance
    private final int ID;  // ID of class instance

    // Private fields //
    private GamePanel gamePanel;

    // Public fields //

    // Singleton of Game class //
    private Game()
    {
        ID = lastID;
        lastID += 1;

        initWindow();
    }
    // Returns instance of game
    public static Game getInstance()
    {
        if (Instance == null)
        {
            Instance = new Game();
        }

        return Instance;
    }
    // Initializing window
    private void initWindow()
    {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle(NAME);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        this.setVisible(true);
    }
    // Run method
    public void run()
    {
        gamePanel = new GamePanel();
        this.add(gamePanel);
        gamePanel.run();
    }

}
