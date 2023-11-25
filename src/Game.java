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
    private Equation equation;
    private Graph graph;


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
        running = true; // DO NOT DELETE

        graph = new Graph(new Point(500, 500), 50, 50);

        equation = Equation.createEquation(EquType.SIN);
        equation.setB(canvas.getWidth() / graph.getxGridInterval() / 2);
        equation.resize(canvas.getWidth() / graph.getxGridInterval() * 100);

        equation.setInterval(-(double)graph.getxCenter()/(double)canvas.getWidth(), 1.-(double)graph.getxCenter()/(double)canvas.getWidth());
        equation.calculateValues();

        timer = new Timer();
        timer.start();

        double elapsedTime;
        double repaintTime = 0;

        final double repaintTimeInterval = 0.016;

        Graphics2D g = (Graphics2D)canvas.getGraphics();

        double scale = 0;

        double scaleTime = 0;
        final double scaleTimeInterval = 0.5;

        while(running)
        {
            try { Thread.sleep(2); }
            catch (Exception e) { e.printStackTrace(); }

            elapsedTime = timer.getElapsedTime_ms() / 1_000;

            repaintTime += elapsedTime;
            if (repaintTime >= repaintTimeInterval)
            {
                repaintTime = 0;
                paint(g);
            }

        }
    }

    private void paint(Graphics2D g)
    {
        int W = canvas.getHeight();
        int H = canvas.getHeight();

        int xCenter = graph.getxCenter();
        int yCenter = H - graph.getyCenter();

        g.clearRect(0, 0, W, H);

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(4));
        g.drawLine(0, yCenter, W, yCenter); // Drawing X-Axis
        g.drawLine(xCenter, 0, xCenter, H); // Drawing Y-Axis

        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(1));

        for (int i = xCenter % graph.getxGridInterval(); i < W; i += graph.getxGridInterval())
        {
            g.drawLine(i, 0, i, H);
        }

        for (int i = yCenter % graph.getyGridInterval(); i < H; i += graph.getyGridInterval())
        {
            g.drawLine(0, i, W, i);
        }

        g.fillOval(xCenter - 7, yCenter -7, 14, 14);

        g.setColor(Color.BLUE);

        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

        for (int i = 0; i < equation.getLength(); i += 1)
        {
            x1 = (int)(equation.getX(i) * W) + xCenter;
            y1 = (int)(yCenter-equation.getY(i)*graph.getyGridInterval());

            if (i != 0)
            {
                g.drawLine(x1, y1, x2, y2);
            }

            x2 = x1;
            y2 = y1;
        }



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
