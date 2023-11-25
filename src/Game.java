package src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    private JPanel canvas;
    private BufferedImage buffor;
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

        canvas = new JPanel();
        canvas.setSize(1000, 1000);

        buffor = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

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
        equation.setB(canvas.getWidth() / graph.getxGridInterval() / 4);
        equation.resize(canvas.getWidth() / graph.getxGridInterval() * 10);

        equation.setInterval(-(double)graph.getxCenter()/(double)canvas.getWidth(), 1.-(double)graph.getxCenter()/(double)canvas.getWidth());
        equation.calculateValues();

        timer = new Timer();
        timer.start();

        double elapsedTime;
        double repaintTime = 0;

        final double repaintTimeInterval = 0.2;

        Graphics2D g_canvas = (Graphics2D)canvas.getGraphics();
        Graphics2D g_buffor = buffor.createGraphics();

        //g_buffor.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double scale = 0;

        double scaleTime = 0;
        final double scaleTimeInterval = 0.02;

        while(running)
        {

            elapsedTime = timer.getElapsedTime_ms() / 1_000;

            scaleTime += elapsedTime;
            if (scaleTime >= scaleTimeInterval)
            {
                scaleTime = 0;
                scale += 0.01;
                equation.setB(scale);
            }

            repaintTime += elapsedTime;
            if (repaintTime >= repaintTimeInterval)
            {
                repaintTime = 0;

                frame(g_buffor);

                if (buffor != null)
                {
                    paint(g_canvas);
                }
            }

        }
    }

    private void frame(Graphics2D g)
    {
        int W = canvas.getHeight();
        int H = canvas.getHeight();

        int xCenter = graph.getxCenter();
        int yCenter = H - graph.getyCenter();

        g.clearRect(0, 0, W, H);

        g.setBackground(new Color(235, 230, 210));

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(3));
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
        g.setStroke(new BasicStroke(2));

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

    private void paint(Graphics2D g)
    {
        g.drawImage(buffor, 0, 0, canvas);
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
