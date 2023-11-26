package src;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel
{

    public final int plotWidth = 1000;
    public final int plotHeight = 1000;

    //// Private fields ////
    private Timer timer;
    private Equation equation;
    private Graph graph;
    private double framesPerSecond = 0;
    private final double maxFPS = 60;

    private boolean running = false;

    public void gameLoop()
    {
        timer = new Timer(512);

        double totalElapsedTime_ms;

        double repaintInterval = 1000. / maxFPS;
        double repaintNewInterval = repaintInterval;

        double fpsInterval = 1000.;
        double fpsNewInterval = fpsInterval;

        int drawCount = 0;

        timer.getElapsedTicks();
        while(running)
        {
            totalElapsedTime_ms = timer.getTotalElapsedTime_ms();

            update();

            if (totalElapsedTime_ms >= repaintNewInterval)
            {
                repaintNewInterval = totalElapsedTime_ms + repaintInterval;
                repaint();
                drawCount += 1;
            }

            if (totalElapsedTime_ms >= fpsNewInterval)
            {
                fpsNewInterval = totalElapsedTime_ms + fpsInterval;
                framesPerSecond = (double)drawCount / (double)fpsInterval * 1_000.;
                drawCount = 0;
            }

        }

        clean();

    }
    public void update()
    {

    }
    public void initPanel()
    {
        this.setSize(new Dimension(plotWidth, plotHeight));
        this.setLocation(0, 0);
    }

    public void run() {

        graph = new Graph(0, 0, 0.1, 0.1);
        equation = Equation.createEquation(EquType.SIN);

        initPanel();

        running = true;

        gameLoop();
    }

    public final Color plotColor = new Color(187, 185, 157);
    public final Color mainGridColor = new Color(0, 0, 0);
    public final Color subGridColor = new Color(61, 57, 57);
    public final Color functionColor = new Color(33, 77, 157);
    public final Color ballColor = new Color(215, 40, 40);
    public final Color debugColor = new Color(19, 150, 23);

    double plotScale = 1.0;
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(plotColor);
        g2.fillRect(0, 0, plotWidth, plotHeight);

        g2.setColor(mainGridColor);

        g2.setColor(subGridColor);

        g2.setColor(functionColor);

        g2.setColor(ballColor);

        g2.setColor(debugColor);
        g2.drawString("FPS: " + framesPerSecond, 0, 10);

        g.dispose();

    }

    //// Private methods ////

    private void clean()
    {

    }

    /*
    private void paint(Graphics2D g)
    {
        int xCenter = graph.getxCenter()*bufforScale;
        int yCenter = H - graph.getyCenter()*bufforScale;

        int xInterval = graph.getxGridInterval()*bufforScale;
        int yInterval = graph.getyGridInterval()*bufforScale;

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(3*bufforScale));
        g.drawLine(0, yCenter, W, yCenter); // Drawing X-Axis
        g.drawLine(xCenter, 0, xCenter, H); // Drawing Y-Axis

        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(bufforScale));

        for (int i = xCenter % xInterval; i < W; i += xInterval)
        {
            g.drawLine(i, 0, i, H);
        }

        for (int i = yCenter % yInterval; i < H; i += yInterval)
        {
            g.drawLine(0, i, W, i);
        }

        g.fillOval(xCenter - 7, yCenter -7, 14, 14);

        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2*bufforScale));

        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

        for (int i = 0; i < equation.getLength(); i += 1)
        {
            x1 = (int)(equation.getX(i) * W) + xCenter;
            y1 = (int)(yCenter-equation.getY(i)*yInterval);

            if (i != 0)
            {
                g.drawLine(x1, y1, x2, y2);
            }

            x2 = x1;
            y2 = y1;
        }

    }
     */

}
