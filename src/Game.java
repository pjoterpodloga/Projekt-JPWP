package src;

import javax.swing.*;
import java.awt.*;

public class Game
{
    //// Window constants ////
    public static final int WIDTH = 1280;   // Width of window
    public static final int HEIGHT = 1024;  // Height of window
    public static String NAME = "Szalone wykresy TEST"; // Title of window
    protected static Game Instance; // Instance of game
    protected static int lastID = 1; // Last ID of instance

    //// Private fields ////
    private Window frame;
    private Canvas canvas;
    private Timer timer;
    private boolean running = false; // Running state of the game
    private final int errorCode = 0; // Error code changes if game cause error
    private final int ID;  // ID of class instance

    //// Public fields ////

    //// Singleton of Game class ////
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
        frame = new Window(NAME);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        frame.setLocation((screenWidth - WIDTH)/4 , (screenHeight - HEIGHT)/4 - 100);

        frame.setLayout(null);

        canvas = new Canvas();
        canvas.setBackground(new Color(235, 230, 210));
        canvas.setSize(1000, 1000);

        frame.getContentPane().add(canvas);

    }

    // Cleanup routine
    private void clean()
    {
        running = false; // DO NOT DELETE

        timer.close();  // Closing timer

        frame.dispose();
        frame.setVisible(false);    // Closing window
    }

    // Game-loop implementation
    private void gameLoop()
    {
        System.out.println("Game works.");

        timer = new Timer();

        double elapsedTime;

        timer.start();

        Graphics2D g = (Graphics2D)canvas.getGraphics();

        while(running)
        {

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            elapsedTime = timer.getElapsedTime_ms() / 1_000;

            scale = scale + elapsedTime * 0.1;

            paint(g);

        }
    }

    double scale = 0;
    private void paint(Graphics2D g)
    {

        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int W = canvas.getHeight();
        int H = canvas.getHeight();

        Point gc = new Point(W/2, H - H/2); // Mock

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawLine(0, gc.y, W, gc.y);       // Draw x-axis
        g.drawLine(gc.x, 0,gc.x, H);        // Draw y-axis

        g.fillOval(gc.x - 7, gc.y - 7, 14, 14); // Draw center of graph

        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(1));

        int W_spacing = Math.round(W / 20);
        int H_spacing = Math.round(H / 20);

        for (int i = (gc.x % W_spacing); i < W; i += W_spacing)
        {
            g.drawLine(i, 0, i, H);
        }

        for (int i = (gc.y % H_spacing); i < W; i += H_spacing)
        {
            g.drawLine(0, i, W, i);
        }

        /*
        int n = 1000;
        double[] x = Utilis.linspace(0 - gc.x, W - gc.x, n);

        double[] y = new double[n];

        g.setColor(Color.blue);

        for(int i = 0; i < n; i += 1)
        {
            y[i] = gc.y - 333. * Math.cos(2 * Math.PI * (x[i] / H - scale));
            g.fillOval((int) x[i] + gc.x - 1, (int) y[i] - 1, 2, 2);
        }
         */

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

        clean();

        return errorCode;
    }
    public void stop()
    {
        running = false;
    }

    //// Getters ////
    public int getID() { return ID; }

    //// Setters ////

}
