package src;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel
{

    public final int plotWidth = 1000;
    public final int plotHeight = 1000;
    private final double maxFPS = 60;

    //// Private fields ////
    private Timer timer;
    private Equation equation;
    private Graph graph;
    private Ball[] ball;
    private double framesPerSecond = 0;
    private boolean running = false;

    public void gameLoop()
    {
        double totalElapsedTime_ms;
        double dt;

        double repaintInterval = 1000. / maxFPS;
        double repaintNewInterval = repaintInterval;

        double fpsInterval = 1000.;
        double fpsNewInterval = fpsInterval;

        int drawCount = 0;

        double lastElapsedTime_ms = timer.getTotalElapsedTime_ms();
        while(running)
        {
            totalElapsedTime_ms = timer.getTotalElapsedTime_ms();
            dt = (totalElapsedTime_ms - lastElapsedTime_ms)/ 1_000.;
            lastElapsedTime_ms = totalElapsedTime_ms;

            update(dt);


            if (totalElapsedTime_ms >= repaintNewInterval)
            {
                repaintNewInterval = totalElapsedTime_ms + repaintInterval;
                repaint();
                drawCount += 1;
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
    public void update(double dt)
    {
        equation.optimizeSize();
        equation.calculateValues();

        checkCollision();

        ball[0].calculateDisplacement(dt);
    }
    public void checkCollision()
    {
        double r = ball[0].getRadius() / 2;
        int x1 = (int)(ball[0].getxPos() * xGridInterval + xCenter), y1 = (int)(yCenter - ball[0].getyPos() * yGridInterval), x2, y2;

        int index = -1;

        double dx, dy, minD = r + 4, d;

        for (int i = 0; i < equation.getLength(); i += 1)
        {
            x2 = (int)(equation.getX(i) * xGridInterval) + xCenter;
            y2 = (int)(yCenter-equation.getY(i)*yGridInterval);

            dx = x1 - x2;
            dy = y1 - y2;

            // Calculate norm
            d = Math.sqrt(dx*dx + dy*dy);

            if (minD > d)
            {
                minD = d;
                index = i;
            }
        }

        double x, y, dx1, dx2, dy1, dy2, vx, vy;

        // Calculate derivative in point
        if (minD <= r + 0.1)
        {
            vx = equation.getX(index) - ball[0].getxPos();
            vy = equation.getY(index) - ball[0].getyPos();

            d = Math.sqrt(vx * vx + vy * vy) * 1.1;

            vx = -vx/d * ball[0].getVelocityModulus();
            vy = -vy/d * ball[0].getVelocityModulus();

            ball[0].setxVelocity(vx);
            ball[0].setyVelocity(vy);

        }

    }
    public void initPanel()
    {
        this.setSize(new Dimension(plotWidth, plotHeight));
        this.setLocation(0, 0);
    }

    public void initPeripherals()
    {
        graph = new Graph(-0.1, -0.1, 0.1, 0.1);

        this.width = this.getWidth();
        this.height = this.getHeight();
        this.xCenter = (int)(width * (0.5 + graph.getdx()));
        this.yCenter = (int)(height * (1. - (0.5 + graph.getdy())));
        this.xGridInterval = (int)(width * graph.getxGridInterval());
        this.yGridInterval = (int)(height * graph.getyGridInterval());

        equation = Equation.createEquation(EquType.SIN);
        equation.setInterval((double) -xCenter / xGridInterval, (double) (width - xCenter) /xGridInterval);
        equation.optimizeSize();

        timer = new Timer((int)maxFPS);

        ball = new Ball[1];
        ball[0] = new Ball(10);
        ball[0].setyAcceleration(-9.81 / 10.);
        ball[0].setxPos(4);
        ball[0].setyPos(4);
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

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

        for(int i = yCenter % yGridInterval, j = -yCenter / yGridInterval; i <= width; i += yGridInterval)
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


        int d = ball[0].getRadius() * 2;
        int x = (int)(ball[0].getxPos() * xGridInterval + xCenter), y = (int)(yCenter - ball[0].getyPos() * yGridInterval);

        g2.setColor(ballColor);
        g2.fillOval(x - d/2, y - d/2, d, d);

        g2.setColor(debugColor);
        g2.drawString("FPS: " + framesPerSecond, 0, 10);

        g.dispose();
    }

    //// Private methods ////

    private void clean()
    {

    }

}
