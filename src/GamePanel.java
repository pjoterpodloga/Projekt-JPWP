package src;

import src.Equations.EquType;
import src.Equations.Equation;
import src.Utilities.Graph;
import src.Utilities.Timer;
import src.Utilities.Vector3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
    private GameState currentGameState;
    private GameState lastGameState;

    private JButton StartStopButton;
    private BufferedImage StartIcon;
    private BufferedImage StopIcon;
    private void initPanel()
    {
        this.setSize(new Dimension(plotWidth, plotHeight));
        this.setLocation(0, 0);
        this.setLayout(null);
        this.setOpaque(false);
    }
    private void initPeripherals()
    {
        URL startImgURL = getClass().getResource("../res/StartPrzycisk.png");
        URL stopImgURL = getClass().getResource("../res/StopPrzycisk.png");

        try {
            StartIcon = ImageIO.read(startImgURL);
            StopIcon = ImageIO.read(stopImgURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StartStopButton = new JButton("");
        StartStopButton.setSize(50, 50);
        StartStopButton.setLocation(925, 25);
        StartStopButton.setOpaque(false);

        StartStopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (currentGameState == GameState.WAITING)
                {
                    currentGameState = GameState.RUNNING;
                }
                else if(currentGameState == GameState.RUNNING)
                {
                    currentGameState = GameState.FAILED;
                }
            }
        });

        this.add(StartStopButton);

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
        ball[0].setResetPos(new Vector3D(4, 4, 0));
        ball[0].setAcceleration(gravity);
        ball[0].reset();
    }
    public void run()
    {
        running = true;

        currentGameState = GameState.WAITING;
        lastGameState = GameState.RUNNING;

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


            if(currentGameState == GameState.WAITING)
            {
                //disable update
                lastUpdate = 0;
            }
            else if(currentGameState == GameState.RUNNING)
            {
                //disable changing equation
            }
            else if(currentGameState == GameState.FAILED)
            {
                //reset ball
                ball[0].reset();
                currentGameState = GameState.WAITING;
            }

            if (totalElapsedTime_ms >= updateNewInterval)
            {
                updateNewInterval = totalElapsedTime_ms + updateInterval;
                update(lastUpdate);
                lastUpdate = 0;
            }

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
    private void update(double dt)
    {
        equation.optimizeSize();
        equation.calculateValues();

        for (Ball value : ball) {
            value.calculateDisplacement(dt);
            checkCollision(value);
        }
    }

    private void checkCollision(Ball b)
    {

        double xBall = b.getxPos(), yBall = b.getyPos();
        double dsq = -1, minDsq = -1, angle = -1, maxAngle = -1;

        int nearestEquIdx = -1, angleEquIdx = -1;

        Vector3D vn = new Vector3D(0), ballVelocity = b.getVelocityVector();

        for (int equ_it = 1; equ_it < equation.getLength() - 1; equ_it += 1)
        {
            vn.x = equation.getX(equ_it) - xBall;
            vn.y = equation.getY(equ_it) - yBall;

            dsq = Vector3D.dot(vn, vn);

            angle = Math.abs(Vector3D.dot(ballVelocity, vn));

            if (equ_it == 1)
            {
                maxAngle = angle;
                angleEquIdx = equ_it;

                minDsq = dsq;
                nearestEquIdx = equ_it;
            }

            if (dsq < minDsq)
            {
                minDsq = dsq;
                nearestEquIdx = equ_it;
            }
        }

        int hrange = 250;
        int equ_it_min = Math.max(1, nearestEquIdx - hrange);
        int equ_it_max = Math.min(equation.getLength(), nearestEquIdx + hrange);

        Vector3D tmpVN = new Vector3D(0);

        for (int equ_it = equ_it_min; equ_it < equ_it_max; equ_it += 1)
        {
            vn.x = equation.getX(equ_it) - xBall;
            vn.y = equation.getY(equ_it) - yBall;

            angle = Vector3D.dot(ballVelocity, vn);

            if (equ_it == equ_it_min)
            {
                maxAngle = angle;
                angleEquIdx = equ_it;

                tmpVN.x = vn.x;
                tmpVN.y = vn.y;
            }

            if (angle > maxAngle)
            {
                maxAngle = angle;
                angleEquIdx = equ_it;

                tmpVN.x = vn.x;
                tmpVN.y = vn.y;
            }
        }

        if (Math.sqrt(minDsq) <= b.getRadius())
        {
            maxAngle = maxAngle / (Vector3D.norm(tmpVN)*Vector3D.norm(ballVelocity));

            int idx = angleEquIdx;

            if (maxAngle < 0.97)
            {
                idx = nearestEquIdx;
            }

            double dx = equation.getX(idx + 1) - equation.getX(idx - 1);
            double dy = equation.getY(idx + 1) - equation.getY(idx - 1);

            Vector3D a = Vector3D.normalized(new Vector3D(dx, dy, 0));

            Vector3D n = Vector3D.cross(a, new Vector3D(0, 0, -1));
            Vector3D d = Vector3D.normalized(new Vector3D(equation.getX(idx) - xBall, equation.getY(idx) - yBall, 0));

            b.calculateBounce(n, d);

            System.out.println("Nearest Equation Index: " + nearestEquIdx + " | Minimal Distance: " + Math.sqrt(minDsq));
            System.out.println("Attack angle Index: " + angleEquIdx + " | Attack angle: " + maxAngle);
            System.out.println("VeloX: " + ballVelocity.x + " | VeloY: " + ballVelocity.y + "\n");
        }

        /*
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

            b.calculateBounce(n, d);
        }
        */

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
        g2.drawLine(x, y, x + (int)(ball[0].getVelocityVector().x * gridAvg * 0.1), y - (int)(ball[0].getVelocityVector().y * gridAvg * 0.1));

        if (currentGameState == GameState.WAITING)
        {
            g2.drawImage(StartIcon, 925, 25, 50, 50, null);
        }
        else
        {
            g2.drawImage(StopIcon, 925, 25, 50, 50, null);
        }

        g.dispose();
    }

    //// Private methods ////
    private void clean()
    {

    }

}
