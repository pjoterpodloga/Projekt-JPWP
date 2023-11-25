package src;

import javax.swing.*;
import java.awt.*;

public class Game extends Canvas
{
    public static final int WIDTH = 1280;   // Width of window
    public static final int HEIGHT = 1024;  // Height of window
    public static String NAME = "Szalone wykresy TEST"; // Title of window
    protected static Game Instance; // Instance of game
    protected static int lastID = 1; // Last ID of instance
    private JFrame frame;

    //// Private fields ////
    private boolean running = false; // Running state of the game
    private int errorCode = 0; // Error code changes if game cause error

    //// Public fields ////
    public int ID = 0;  // ID of class instance

    //// Singleton of Game class ////
    //
    protected Game()
    {
        ID = lastID;
        lastID += 1;
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

    //// Private methods ////

    private void initWindow()
    {

    }

    // Game-loop implementation
    private void gameLoop()
    {
        System.out.println("Gra dziala.");

        Timer timer = new Timer();
        timer.start();

        long time = 0;
        long elapsedTime = 0;

        while(running)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            elapsedTime = timer.getElapsedTime() / 1_000_000;
            time = time + elapsedTime;

            System.out.print("Elapsed time in ms:");
            System.out.println(elapsedTime);
            System.out.println("Integrated time in ms:");
            System.out.println(time);

            if ( time >= 10_000)
            {
                break;
            }
        }

        timer.close();
    }

    //// Public methods ////

    // Return state of game-loop
    public boolean isRunning()
    {
        return running;
    }

    // Game-loop method
    public int run()
    {
        initWindow();

        running = true; // DO NOT DELETE

        gameLoop();

        running = false; // DO NOT DELETE

        return errorCode;
    }

    public void stop()
    {
        running = false;
    }

}
