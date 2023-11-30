package src;

import src.Equations.EquType;
import src.Equations.Equation;
import src.Utilities.Graph;
import src.Utilities.Timer;
import src.Utilities.Vector3D;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel
{

    //// Private fields ////
    private final int plotWidth = 1000;
    private final int plotHeight = 1000;
    private final double maxFPS = 60;
    private src.Utilities.Timer timer;
    private Equation equation;
    private Graph graph;
    private Ball[] ball;
    private double framesPerSecond = 0;
    private int width;
    private int height;
    private int xCenter;
    private int yCenter;
    private int xGridInterval;
    private int yGridInterval;
    private double plotScale = 1.0;
    private Vector3D gravity = new Vector3D(0, -9.81, 0);
    private boolean running = false;

    private void initPanel()
    {
        this.setSize(new Dimension(plotWidth, plotHeight));
        this.setLocation(0, 0);
    }
    private void initPeripherals()
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

        timer = new Timer((int)maxFPS * 4);

        ball = new Ball[1];
        ball[0] = new Ball(0.1);
        ball[0].setxPos(4);
        ball[0].setyPos(4);
    }
    public void run()
    {
        running = true;

        initPanel();
        initPeripherals();
        gameLoop();
    }
    private void gameLoop()
    {
        double totalElapsedTime_ms;

        double updateInterval = 1.;
        double updateNewInterval = updateInterval;
        double lastUpdate = 0;

        double repaintInterval = 1000. / maxFPS;
        double repaintNewInterval = repaintInterval;

        double fpsInterval = 1000.;
        double fpsNewInterval = fpsInterval;

        int drawCount = 0;

        double lastElapsedTime_ms = timer.getTotalElapsedTime_ms();
        while(running)
        {
            totalElapsedTime_ms = timer.getTotalElapsedTime_ms();
            lastUpdate += (totalElapsedTime_ms - lastElapsedTime_ms)/ 1_000.;
            lastElapsedTime_ms = totalElapsedTime_ms;


            if (totalElapsedTime_ms >= updateNewInterval)
            {
                updateNewInterval = totalElapsedTime_ms + updateInterval;
                update(lastUpdate / 2);
                lastUpdate = 0;
            }

            if (totalElapsedTime_ms >= repaintNewInterval)
            {
                repaintNewInterval = totalElapsedTime_ms + repaintInterval;
                repaint();
                drawCount += 1;
                //equation.setC(equation.getC() + 0.001);
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
    private void update(double dt)
    {
        equation.optimizeSize();
        equation.calculateValues();

        for (Ball value : ball) {
            value.calculateDisplacement(dt);
            checkCollision(value);
        }
    }
    // TODO: Need to think of other way to calculate collision, bounce and roll
    // TODO: Add physics and material properties class
    private void checkCollision(Ball b)
    {
        double xBall = b.getxPos(), yBall = b.getyPos();

        Vector3D vn = new Vector3D(0);

        double r = b.getRadius(), dsq, minDsq = r * r + 0.5;
        double maxAttackAngle = 0, aaEpsilon = 0.999;
        int indexAA = -1, indexClosest = -1;

        Vector3D veloVectorBall = Vector3D.normalized(b.getVelocityVector());

        for (int i = 1; i < equation.getLength() - 1; i += 1)
        {
            vn.x = equation.getX(i) - xBall;
            vn.y = equation.getY(i) - yBall;

            dsq = Vector3D.dot(vn, vn);

            double attackAngle = Vector3D.dot(veloVectorBall, Vector3D.normalized(vn));

            if (maxAttackAngle <= attackAngle)
            {
                maxAttackAngle = attackAngle;
                if (maxAttackAngle >= aaEpsilon)
                {
                    indexAA = i;
                }
            }

            if (minDsq > dsq)
            {
                minDsq= dsq;
                indexClosest = i;
            }
        }

        int index = indexAA;

        if (indexAA == -1)
        {
            index = indexClosest;
        }

        Vector3D n = null, d = null;

        if (index != -1)
        {
            d = Vector3D.normalized(new Vector3D(equation.getX(index) - xBall, equation.getY(index) - yBall, 0));
        }

        // Calculate vector of bounce
        if (Math.sqrt(minDsq) <= r && b.getHitCountdown() <= 0)
        {
            b.hit();

            double dx = equation.getX(index + 1) - equation.getX(index - 1);
            double dy = equation.getY(index + 1) - equation.getY(index - 1);

            double a = dy/dx;

            n = Vector3D.normalized(Vector3D.cross(new Vector3D(1, a, 0), new Vector3D(0, 0, -1)));
        }

        b.physicsProperties.calculateAcceleration(gravity, n, d);
    }
    private final Color plotColor = new Color(187, 185, 157);
    private final Color mainGridColor = new Color(0, 0, 0);
    private final Color subGridColor = new Color(61, 57, 57);
    private final Color functionColor = new Color(33, 77, 157);
    private final Color ballColor = new Color(215, 40, 40);
    private final Color debugColor = new Color(19, 150, 23);
    private int mainGridSize = 3;
    private int subGridSize = 2;
    private  int functionSize = 2;
    @Override
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(plotColor);
        g2.fillRect(0, 0, plotWidth, plotHeight);

        g2.setColor(mainGridColor);
        g2.setStroke(new BasicStroke(mainGridSize));
        g.drawLine(0, yCenter, width, yCenter);     // x-axis
        g.drawLine(xCenter, 0, xCenter, height);    // y-axis
        int centerSize = 12;
        g.fillOval(xCenter - centerSize /2, yCenter - centerSize /2, centerSize, centerSize);

        g2.setColor(subGridColor);
        g2.setStroke(new BasicStroke(subGridSize));

        for(int i = xCenter % xGridInterval, j = -xCenter / xGridInterval; i <= width; i += xGridInterval)
        {
            g2.drawLine(i, 0, i, height);
            g2.drawString(""+ j++, i + 4, yCenter + 14);
        }

        for(int i = yCenter % yGridInterval, j = -yCenter / yGridInterval; i <= width; i += yGridInterval)
        {
            g2.drawLine(0, i, width, i);
            if (j == 0) { j++; continue;}
            g2.drawString(""+ -j++, xCenter + 10, i - 4);
        }

        g2.setColor(functionColor);
        g2.setStroke(new BasicStroke(functionSize));

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

        double gridAvg = Math.sqrt(xGridInterval * yGridInterval);
        int d = (int)(ball[0].getRadius() * gridAvg);
        int x = (int)(ball[0].getxPos() * xGridInterval + xCenter), y = (int)(yCenter - ball[0].getyPos() * yGridInterval);

        g2.setColor(ballColor);
        g2.fillOval(x - d, y - d, 2*d, 2*d);

        g2.setColor(debugColor);
        g2.drawString("FPS: " + framesPerSecond, 0, 10);
        g2.drawLine(x, y, x + (int)(ball[0].physicsProperties.getVelocity().x * gridAvg * 0.1), y - (int)(ball[0].physicsProperties.getVelocity().y * gridAvg * 0.1));

        g.dispose();
    }

    //// Private methods ////
    private void clean()
    {

    }

}
