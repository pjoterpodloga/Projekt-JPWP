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
        double totalElapsedTime_ms;

        double repaintInterval = 1000. / maxFPS;
        double repaintNewInterval = repaintInterval;

        double fpsInterval = 1000.;
        double fpsNewInterval = fpsInterval;

        int drawCount = 0;

        double scaleB = 0.1;

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

                scaleB += 0.1;
                equation.setA(scaleB);
                equation.setB(scaleB);
            }

            if (totalElapsedTime_ms >= fpsNewInterval)
            {
                fpsNewInterval = totalElapsedTime_ms + fpsInterval;
                framesPerSecond = (double)drawCount / fpsInterval * 1_000.;
                drawCount = 0;
            }

        }

        clean();

    }
    public void update()
    {
        equation.optimazeSize();
        equation.calculateValues();
    }
    public void initPanel()
    {
        this.setSize(new Dimension(plotWidth, plotHeight));
        this.setLocation(0, 0);
    }

    public void initPeripherals()
    {
        graph = new Graph(0., 0., 0.1, 0.1);

        this.width = this.getWidth();
        this.height = this.getHeight();
        this.xCenter = (int)(width * (0.5 + graph.getdx()));
        this.yCenter = (int)(height * (1. - (0.5 + graph.getdy())));
        this.xGridInterval = (int)(width * graph.getxGridInterval());
        this.yGridInterval = (int)(height * graph.getyGridInterval());

        equation = Equation.createEquation(EquType.SIN);
        equation.setInterval((double) -xCenter / xGridInterval, (double) (width - xCenter) /xGridInterval);
        equation.optimazeSize();

        timer = new Timer(512);
    }

    private int width;
    private int height;
    private int xCenter;
    private int yCenter;
    private int xGridInterval;
    private int yGridInterval;

    public void run()
    {
        initPanel();
        initPeripherals();

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
        g2.setStroke(new BasicStroke(3));
        g.drawLine(0, yCenter, width, yCenter);     // x-axis
        g.drawLine(xCenter, 0, xCenter, height);    // y-axis
        int centerSize = 12;
        g.fillOval(xCenter - centerSize /2, yCenter - centerSize /2, centerSize, centerSize);

        g2.setColor(subGridColor);
        g2.setStroke(new BasicStroke(2));

        for(int i = xCenter % xGridInterval, j = -xCenter / xGridInterval; i <= width; i += xGridInterval)
        {
            g2.drawLine(i, 0, i, height);
            g2.drawString(""+ j++, i + 4, yCenter + 14);
        }

        for(int i = yCenter % yGridInterval, j = -yCenter / xGridInterval; i <= width; i += yGridInterval)
        {
            g2.drawLine(0, i, width, i);
            if (j == 0) { j++; continue;}
            g2.drawString(""+ j++, xCenter + 10, i - 4);
        }

        g2.setColor(functionColor);
        g2.setStroke(new BasicStroke(2));

        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

        for (int i = 0; i < equation.getLength(); i += 1)
        {
            x1 = (int)(equation.getX(i) * xGridInterval) + xCenter;
            y1 = (int)(yCenter-equation.getY(i)*yGridInterval);

            if (i != 0)
            {
                g2.drawLine(x1, y1, x2, y2);
            }

            x2 = x1;
            y2 = y1;
        }

        g2.setColor(ballColor);

        g2.setColor(debugColor);
        g2.drawString("FPS: " + framesPerSecond, 0, 10);

        g.dispose();
    }

    //// Private methods ////

    private void clean()
    {

    }

}
