package src;

import src.Equations.EquType;
import src.Equations.Equation;
import src.Utilities.Graph;
import src.Utilities.Timer;
import src.Utilities.Vector3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GamePanel extends JPanel
{
    private final boolean DEBUG_MODE = false;

    // Game constants //
    private final int plotWidth = 1000;
    private final int plotHeight = 1000;
    private final double maxFPS = 60;
    // Peripherals //
    private src.Utilities.Timer timer;
    private Equation equation;
    private Graph graph;
    private Ball ball;
    private Target target;
    // Game parameters //
    private final Vector3D gravity = new Vector3D(0, -9.81, 0);
    private double points;
    private double totalPoints;
    private final double maxPoints = 5000;
    private final double minPoints = 1000;
    private double framesPerSecond = 0;
    // Calculated dimensions //
    private int width, height;
    private int xCenter, yCenter;
    private int xGridInterval, yGridInterval;
    private double xMinBound, xMaxBound, yMinBound, yMaxBound;
    // Game flags //
    private boolean running = false;
    private boolean generateLevel = false;
    private boolean buttonsEnabled = true;
    private boolean winConditionDetected;
    private boolean stuckDetected;
    private boolean outOfBoundsDetected;
    private boolean collisionDetected;
    private GameState currentGameState;
    private GameMode gameMode = GameMode.LEVELS;
    // Levels //
    private Level[] levels;
    private int levelNumber;
    // Equation parameters sliders //
    private JSlider parameterASlider, parameterBSlider, parameterCSlider;
    private JSlider parameterDSlider, parameterESlider, parameterFSlider;
    private JSlider parameterGSlider;
    // Equation type buttons
    private JButton poly1Button, poly2Button, poly3Button, poly4Button;
    private JButton sinButton, expButton;
    // Menu buttons //
    private JButton menuButton;
    private JButton sandboxButton, levelsButton;
    // Start-Stop button //
    private JButton StartStopButton;
    private BufferedImage StartIcon, StopIcon;

    // Game panel initialization
    private void initPanel()
    {
        this.setSize(new Dimension(1280, plotHeight));
        this.setLocation(0, 0);
        this.setLayout(null);
        this.setOpaque(false);
    }
    // Peripheral initialization
    private void initPeripherals()
    {

        timer = new Timer((int)maxFPS * 4);

        graph = new Graph(-0.1, -0.1, 0.1, 0.1);

        calculateDimensions(graph);
        calculateEquation(EquType.POLY2);

        ball = new Ball(0.1);
        ball.setResetPos(new Vector3D(4, 4, 0));
        ball.setAcceleration(gravity);
        ball.reset();

        target = new Target(0.5, new Vector3D(-2.1,-2.4,0));

        points = 0;
        totalPoints = 0;

        loadLevels();

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

                if (currentGameState == GameState.MENU)
                {

                }
                else if (currentGameState == GameState.WAITING)
                {
                    currentGameState = GameState.RUNNING;
                }
                else if(currentGameState == GameState.RUNNING || currentGameState == GameState.FAILED)
                {
                    currentGameState = GameState.WAITING;
                }
            }
        });

        parameterASlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getA() * 10));
        parameterASlider.setLocation(1090, 0);
        parameterASlider.setSize(180, 50);
        parameterASlider.setVisible(true);

        parameterASlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setA(value);
            }
        });

        parameterBSlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getB() * 10));
        parameterBSlider.setLocation(1090, 50);
        parameterBSlider.setSize(180, 50);
        parameterBSlider.setVisible(true);

        parameterBSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setB(value);
            }
        });

        parameterCSlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getC() * 10));
        parameterCSlider.setLocation(1090, 100);
        parameterCSlider.setSize(180, 50);
        parameterCSlider.setVisible(true);

        parameterCSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setC(value);
            }
        });

        parameterDSlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getD() * 10));
        parameterDSlider.setLocation(1090, 150);
        parameterDSlider.setSize(180, 50);
        parameterDSlider.setVisible(true);

        parameterDSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setD(value);
            }
        });

        parameterESlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getE() * 10));
        parameterESlider.setLocation(1090, 200);
        parameterESlider.setSize(180, 50);
        parameterESlider.setVisible(true);

        parameterESlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setE(value);
            }
        });

        parameterFSlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getF() * 10));
        parameterFSlider.setLocation(1090, 250);
        parameterFSlider.setSize(180, 50);
        parameterFSlider.setVisible(true);

        parameterFSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setF(value);
            }
        });

        parameterGSlider = new JSlider(JSlider.HORIZONTAL, -500, 500, (int)(equation.getG() * 10));
        parameterGSlider.setLocation(1090, 300);
        parameterGSlider.setSize(180, 50);
        parameterGSlider.setVisible(true);

        parameterGSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((JSlider) e.getSource()).getValue() / 100.;
                equation.setG(value);
            }
        });

        poly1Button = new JButton();
        poly1Button.setSize(270, 50);
        poly1Button.setText("Ax^1 + B");
        poly1Button.setLocation(1000, 350);
        poly1Button.setVisible(true);

        poly1Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.POLY1);
            }
        });

        poly2Button = new JButton();
        poly2Button.setSize(270, 50);
        poly2Button.setText("Ax^2 + Bx^1 + C");
        poly2Button.setLocation(1000, 400);
        poly2Button.setVisible(true);

        poly2Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.POLY2);
            }
        });

        poly3Button = new JButton();
        poly3Button.setSize(270, 50);
        poly3Button.setText("Ax^3 + Bx^2 + Cx^1 + D");
        poly3Button.setLocation(1000, 450);
        poly3Button.setVisible(true);

        poly3Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.POLY3);
            }
        });

        poly4Button = new JButton();
        poly4Button.setSize(270, 50);
        poly4Button.setText("Ax^4 + Bx^3 + Cx^2 + Dx^1 + 1");
        poly4Button.setLocation(1000, 450);
        poly4Button.setVisible(true);

        poly4Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.POLY4);
            }
        });

        sinButton = new JButton();
        sinButton.setSize(270, 50);
        sinButton.setText("Asin(2pi*(Bx - C)) + D");
        sinButton.setLocation(1000, 500);
        sinButton.setVisible(true);

        sinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.SIN);
            }
        });


        expButton = new JButton();
        expButton.setSize(270, 50);
        expButton.setText("Ae^(Bx)+C");
        expButton.setLocation(1000, 550);
        expButton.setVisible(true);

        expButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!buttonsEnabled)
                    return;

                calculateEquation(EquType.EXP);
            }
        });

        menuButton = new JButton();
        menuButton.setSize(270, 50);
        menuButton.setText("Menu");
        menuButton.setLocation(1000, 940);
        menuButton.setVisible(true);

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (currentGameState != GameState.MENU)
                {
                    currentGameState = GameState.MENU;
                    disableSliders();
                    disableButtons();
                }
            }
        });

        sandboxButton = new JButton();
        sandboxButton.setSize(270, 50);
        sandboxButton.setLocation(365, 395);
        sandboxButton.setOpaque(false);

        sandboxButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (currentGameState == GameState.MENU)
                {
                    gameMode = GameMode.SANDBOX;
                    currentGameState = GameState.WAITING;

                    enableButtons();
                    enableSliders();

                    generateLevel = true;
                }
            }
        });

        levelsButton = new JButton();
        levelsButton.setSize(270, 50);
        levelsButton.setLocation(365, 485);
        levelsButton.setOpaque(false);


        levelsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (currentGameState == GameState.MENU)
                {
                    gameMode = GameMode.LEVELS;
                    currentGameState = GameState.WAITING;

                    levelNumber = 0;
                    loadLevel(levelNumber);

                    enableSliders();
                }
            }
        });

        this.add(StartStopButton);
        this.add(parameterASlider);
        this.add(parameterBSlider);
        this.add(parameterCSlider);
        this.add(parameterDSlider);
        this.add(parameterESlider);
        this.add(parameterFSlider);
        this.add(parameterGSlider);
        this.add(poly1Button);
        this.add(poly2Button);
        this.add(poly3Button);
        this.add(poly4Button);
        this.add(sinButton);
        this.add(expButton);
        this.add(menuButton);
        this.add(sandboxButton);
        this.add(levelsButton);
    }
    // Generating random level in sandbox
    private void randomLevel()
    {
        generateLevel = false;

        int equationType = (int) Math.floor(Math.random()*(6.9 - 1.) + 1.);

        calculateEquation(EquType.valueOf( equationType));

        double xRange = 0.8 * xMaxBound - 0.8 * xMinBound;
        double yMean = (yMaxBound + yMinBound) / 2.;
        double yRange = 0.8 * yMaxBound - yMean;

        double ballR = 0.1;
        double ballX = Math.random()*(xRange) + 0.8 * yMinBound;
        double ballY = Math.random()*(yRange) + yMean;

        ball = new Ball(ballR);
        ball.setResetPos(new Vector3D(ballX, ballY, 0));
        ball.setAcceleration(gravity);
        ball.reset();

        yRange = (yMean - 0.8*yMinBound);

        double targetR = Math.random() * (0.6 - 0.3) + 0.3;
        double targetX = Math.random()*(xRange) + 0.8 * yMinBound;
        double targetY = Math.random()*(yRange) + 0.8 * yMinBound;

        target = new Target(targetR, new Vector3D(targetX,targetY,0));
    }
    // Loading next prepared level
    private void nextLevel()
    {
        levelNumber++;

        loadLevel(levelNumber);

        if (levelNumber >= levels.length)
            levelNumber = 0;
    }
    // Loading n level
    private void loadLevel(int n)
    {
        calculateEquation(levels[n].equType);

        ball = new Ball(levels[n].ball.z);
        ball.setResetPos(new Vector3D(levels[n].ball.x, levels[n].ball.y, 0));
        ball.setAcceleration(gravity);
        ball.reset();

        target = new Target(levels[n].target.z, new Vector3D(levels[n].target.x,levels[n].target.y,0));
    }
    // Loading prepared levels
    private void loadLevels()
    {
        levels = new Level[3];

        levels[0] = new Level();
        levels[0].equType = EquType.SIN;
        levels[0].ball = new Vector3D(2, 3, 0.1);
        levels[0].target = new Vector3D(-2 , -2, 0.5);
        levels[0].ID = 1;

        levels[1] = new Level();
        levels[1].equType = EquType.POLY2;
        levels[1].ball = new Vector3D(-3, 4, 0.1);
        levels[1].target = new Vector3D(2, -3, 0.5);
        levels[1].ID = 2;

        levels[2] = new Level();
        levels[2].equType = EquType.POLY3;
        levels[2].ball = new Vector3D(3, 4.5, 0.1);
        levels[2].target = new Vector3D(-3 , -3, 0.5);
        levels[2].ID = 3;

        levelNumber = 0;
    }
    // Calculating equation interval of x and optimize length
    private void calculateEquation(EquType equType)
    {
        if (!(currentGameState == GameState.WAITING || currentGameState == GameState.MENU))
            return;

        equation = Equation.createEquation(equType);
        equation.setInterval((double) -xCenter / xGridInterval, (double) (width - xCenter) /xGridInterval);
        equation.optimizeSize();
    }
    // Transforming graph and plot dimensions to game dimensions
    private void calculateDimensions(Graph g)
    {
        this.width = plotWidth;
        this.height = plotHeight;
        this.xCenter = (int)(width * (0.5 + g.getdx()));
        this.yCenter = (int)(height * (1. - (0.5 + g.getdy())));
        this.xGridInterval = (int)(width * g.getxGridInterval());
        this.yGridInterval = (int)(height * g.getyGridInterval());
        this.xMinBound = ((width * (-0.5 - g.getdx())) / xGridInterval);
        this.xMaxBound = ((double) width /xGridInterval + xMinBound);
        this.yMinBound = ((height * (-0.5 - g.getdy())) / yGridInterval);
        this.yMaxBound = ((double) height /yGridInterval + yMinBound);
    }
    // Disabling buttons
    private void disableButtons()
    {
        poly1Button.setEnabled(false);
        poly2Button.setEnabled(false);
        poly3Button.setEnabled(false);
        poly4Button.setEnabled(false);
        sinButton.setEnabled(false);
        expButton.setEnabled(false);

        buttonsEnabled = false;
    }
    // Disabling sliders
    private void disableSliders()
    {
        parameterASlider.setEnabled(false);
        parameterBSlider.setEnabled(false);
        parameterCSlider.setEnabled(false);
        parameterDSlider.setEnabled(false);
        parameterESlider.setEnabled(false);
        parameterFSlider.setEnabled(false);
        parameterGSlider.setEnabled(false);
    }
    // Enable Buttons
    private void enableButtons()
    {
        poly1Button.setEnabled(true);
        poly2Button.setEnabled(true);
        poly3Button.setEnabled(true);
        poly4Button.setEnabled(true);
        sinButton.setEnabled(true);
        expButton.setEnabled(true);

        buttonsEnabled = true;
    }
    // Enable buttons
    private void enableSliders()
    {
        parameterBSlider.setEnabled(true);
        parameterASlider.setEnabled(true);
        parameterCSlider.setEnabled(true);
        parameterDSlider.setEnabled(true);
        parameterESlider.setEnabled(true);
        parameterFSlider.setEnabled(true);
        parameterGSlider.setEnabled(true);
    }
    // Game-loop method
    private void gameLoop()
    {
        double totalElapsedTime_ms;     // Total elapsed time in ms

        double pointsDecayBounce = 2;   // Number of bounces when above points starts to decay

        double winInterval = 3000.;     // Interval in ms of post win condition
        double winNewInterval = winInterval;

        double resetInterval = 2500.;   // Interval in ms of post failure condition
        double resetNewInterval = resetInterval;

        double updateInterval = 1.;     // Interval in ms of updating game
        double updateNewInterval = updateInterval;
        double lastUpdate = 0;
        double timeScale = 1;
        double totalTimeScale = 0;

        double repaintInterval = 1000. / maxFPS;    // Interval in ms of repaint, target 60 frames per second
        double repaintNewInterval = repaintInterval;

        double fpsInterval = 1000.;     // Calculating fps interval in ms, debug purposes
        double fpsNewInterval = fpsInterval;

        int drawCount = 0;      // Count of repaints, debug purposes


        double lastElapsedTime_ms = timer.getTotalElapsedTime_ms(); // Resetting timer
        while(running)
        {
            totalElapsedTime_ms = timer.getTotalElapsedTime_ms();
            lastUpdate += (totalElapsedTime_ms - lastElapsedTime_ms)/ 1_000.;
            lastElapsedTime_ms = totalElapsedTime_ms;

            if (gameMode == GameMode.LEVELS)
            {
                disableButtons();   // Buttons disabled in Levels mode
            }

            if(currentGameState == GameState.WAITING || currentGameState == GameState.MENU)
            {
                // Resetting intervals and states when waiting for start
                winNewInterval = totalElapsedTime_ms + winInterval;
                resetNewInterval = resetInterval + totalElapsedTime_ms;
                lastUpdate = 0;

                totalTimeScale = 0;
                timeScale = 1;

                points = maxPoints;

                if (generateLevel)
                    randomLevel();

                ball.reset();

                collisionDetected = false;
                outOfBoundsDetected = false;
                stuckDetected = false;
                winConditionDetected = false;

                if (currentGameState != GameState.MENU)
                {
                    if (gameMode == GameMode.SANDBOX)
                        enableButtons();
                    enableSliders();
                }
            }
            else if(currentGameState == GameState.RUNNING)
            {
                // Resetting intervals when simulating
                winNewInterval = totalElapsedTime_ms + winInterval;
                resetNewInterval = resetInterval + totalElapsedTime_ms;

                // Disabling buttons and slider when simulating
                disableButtons();
                disableSliders();
            }
            else if(currentGameState == GameState.FAILED)
            {
                // Resetting intervals when simulating
                winNewInterval = totalElapsedTime_ms + winInterval;
                updateNewInterval = totalElapsedTime_ms + updateInterval;
                lastUpdate = 0;
            }
            else if(currentGameState == GameState.WON)
            {
                // Resetting intervals when simulating
                resetNewInterval = resetInterval + totalElapsedTime_ms;
            }

            if (totalElapsedTime_ms >= resetNewInterval)
            {
                // Game state change FAILED -> WAITING
                currentGameState = GameState.WAITING;

                System.out.println("Ball state reset to default.\n");
            }

            if (totalElapsedTime_ms >= winNewInterval)
            {
                // Game state change WON -> WAITING
                currentGameState = GameState.WAITING;
                totalPoints += points;  // Increase total points

                // Generate random level when in sandbox
                // Load next level when in levels
                if (gameMode == GameMode.SANDBOX)
                    randomLevel();
                else
                    nextLevel();
            }

            if (totalElapsedTime_ms >= updateNewInterval)
            {
                // Calculating delta time and updating game
                updateNewInterval = totalElapsedTime_ms + updateInterval;
                update(lastUpdate * timeScale);

                // Scaling points with number of bounces
                double bouncePointScale = Math.max(0, ball.getHitCount() - pointsDecayBounce) * 100;

                if (currentGameState == GameState.RUNNING)
                {
                    points = Math.max(minPoints, maxPoints - bouncePointScale);
                }

                // Slowing simulation time when win condition detected
                if (currentGameState == GameState.WON)
                {
                    totalTimeScale += lastUpdate;
                    timeScale = Math.exp(-totalTimeScale);
                }

                // Clamping min time scale
                if (timeScale <= 0.1)
                {
                    timeScale = 0.1;
                }

                // Resetting delta time
                lastUpdate = 0;
            }

            if (totalElapsedTime_ms >= repaintNewInterval)
            {
                // Repainting
                repaintNewInterval = totalElapsedTime_ms + repaintInterval;
                repaint();
                drawCount += 1;
            }

            if (totalElapsedTime_ms >= fpsNewInterval)
            {
                // Counting frames per second
                fpsNewInterval = totalElapsedTime_ms + fpsInterval;
                framesPerSecond = (double)drawCount / fpsInterval * 1_000.;
                drawCount = 0;
            }
        }
    }
    // Update method
    private void update(double dt)
    {
        equation.optimizeSize();
        equation.calculateValues();

        if (dt == 0)
        {
            return;
        }

        stuckDetected = ball.calculateDisplacement(dt);
        winConditionDetected = checkWinCondition(ball);
        outOfBoundsDetected = checkBounds(ball);
        collisionDetected = checkCollision(ball);

        if (winConditionDetected)
        {
            currentGameState = GameState.WON;

            System.out.println("Ball achieved target.\n");

        }
        else if ((stuckDetected || outOfBoundsDetected) && currentGameState != GameState.WON)
        {
            currentGameState = GameState.FAILED;

            System.out.println("Ball is stuck or out of bounds.\n");
        }
    }
    // Checking for win condition
    private boolean checkWinCondition(Ball b)
    {
        double x = b.getxPos() - target.getPosX(), y = b.getyPos() - target.getPosY();
        double d = Math.sqrt(x*x + y*y);

        return target.getRadius() > d;
    }
    // Checking for fail condition
    private boolean checkBounds(Ball b)
    {
        double xBall = b.getxPos(), yBall = b.getyPos(), rBall = b.getRadius();

        return (xBall + rBall < xMinBound) || (xBall - rBall > xMaxBound) || (yBall + rBall < yMinBound) || (yBall - rBall > yMaxBound);
    }
    // Checking for collision with function. Detecting bounce
    private boolean checkCollision(Ball b)
    {

        double xBall = b.getxPos(), yBall = b.getyPos();
        double dsq, minDsq = -1;

        int nearestEquIdx = -1;

        Vector3D vn = new Vector3D(0), ballVelocity = b.getVelocityVector();

        // Finding nearest point of function
        for (int equ_it = 1; equ_it < equation.getLength() - 1; equ_it += 1)
        {
            vn.x = equation.getX(equ_it) - xBall;
            vn.y = equation.getY(equ_it) - yBall;

            dsq = Vector3D.dot(vn, vn);

            if (equ_it == 1)
            {
                minDsq = dsq;
                nearestEquIdx = equ_it;
            }

            if (dsq < minDsq)
            {
                minDsq = dsq;
                nearestEquIdx = equ_it;
            }
        }

        // If close enough calculate bounce
        if (Math.sqrt(minDsq) <= b.getRadius())
        {
            double dx = equation.getX(nearestEquIdx + 1) - equation.getX(nearestEquIdx - 1);
            double dy = equation.getY(nearestEquIdx + 1) - equation.getY(nearestEquIdx - 1);

            // Derivative in point of function
            Vector3D a = Vector3D.normalized(new Vector3D(dx, dy, 0));

            // Normal vector to surface in point
            Vector3D n = Vector3D.cross(new Vector3D(0, 0, 1), a);
            // Attack vector
            Vector3D d = Vector3D.normalized(b.getVelocityVector());

            // Debug purposes
            lastBounceN.x = a.x;
            lastBounceN.y = a.y;

            // Debug purposes
            lastBouncePos.x = xBall;
            lastBouncePos.y = yBall;

            // Calculating bounce
            b.calculateBounce(n, d);

            // Debug purposes
            System.out.println("n vector X: " + n.x + " | Y: " + n.y);
            System.out.println("d vector X: " + d.x + " | Y: " + d.y);
            System.out.println("a vector X: " + a.x + " | Y: " + a.y);
            System.out.println("Nearest Equation Index: " + nearestEquIdx + " | Minimal Distance: " + Math.sqrt(minDsq));
            System.out.println("VeloX: " + ballVelocity.x + " | VeloY: " + ballVelocity.y + "\n");
        }

        return b.isStuck();
    }


    // Public run method
    public void run()
    {
        running = true;

        currentGameState = GameState.MENU;

        initPanel();
        initPeripherals();
        gameLoop();
    }

    // Debug purposes
    Vector3D lastBouncePos = new Vector3D(0);
    Vector3D lastBounceN = new Vector3D(0);

    // Set of colors, fonts and sizes //
    private final Color plotColor = new Color(187, 185, 157);
    private final Color mainGridColor = new Color(0, 0, 0);
    private final Color subGridColor = new Color(61, 57, 57);
    private final Color functionColor = new Color(33, 77, 157);
    private final Color ballColor = new Color(215, 40, 40);
    private final Color targetFillColor = new Color(20, 180, 232, 155);
    private final Color targetEdgeColor = new Color(20, 112, 232, 155);
    private final Color menuColor = new Color(187, 184, 184, 237);
    private final Color debugColor = new Color(19, 150, 23);
    private final Font parameterFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final Font debugFont = new Font(Font.SANS_SERIF, Font.BOLD, 10);
    private final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final Font menuFont = new Font(Font.SANS_SERIF, Font.BOLD, 40);
    private final int mainGridSize = 3;
    private final int subGridSize = 2;
    private final int functionSize = 2;
    private final int targetEdgeSize = 3;
    // Overrode paint method
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Plane background
        g2.setColor(plotColor);
        g2.fillRect(0, 0, plotWidth, plotHeight);

        // Main axis
        g2.setColor(mainGridColor);
        g2.setStroke(new BasicStroke(mainGridSize));
        g.drawLine(0, yCenter, width, yCenter);     // x-axis
        g.drawLine(xCenter, 0, xCenter, height);    // y-axis
        int centerSize = 12;
        g.fillOval(xCenter - centerSize /2, yCenter - centerSize /2, centerSize, centerSize);

        // Sub grid lines
        g2.setColor(subGridColor);
        g2.setStroke(new BasicStroke(subGridSize));

        // Vertical lines
        for(int i = xCenter % xGridInterval, j = -xCenter / xGridInterval; i < width; i += xGridInterval)
        {
            g2.drawLine(i, 0, i, height);
            g2.drawString(""+ j++, i + 4, yCenter + 14);
        }

        // Horizontal lines
        for(int i = yCenter % yGridInterval, j = -yCenter / yGridInterval; i < height; i += yGridInterval)
        {
            g2.drawLine(0, i, width, i);
            if (j == 0) { j++; continue;}
            g2.drawString(""+ -j++, xCenter + 10, i - 4);
        }

        // Parameters of function
        g2.setFont(parameterFont);

        for(int i = 0; i < 7; i += 1)
        {
            g2.drawString((char)(65 + i) + ": " + equation.getParameter(i), 1010, 50 * (i + 1) - 20);
        }

        // Function
        g2.setColor(functionColor);
        g2.setStroke(new BasicStroke(functionSize));

        int x1, x2 = 0, y1, y2 = 0;

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

        // Ball
        double gridAvg = Math.sqrt(xGridInterval * yGridInterval);
        int rBall = (int)(ball.getRadius() * gridAvg);
        int xBall = (int)(ball.getxPos() * xGridInterval + xCenter), yBall = (int)(yCenter - ball.getyPos() * yGridInterval);

        g2.setColor(ballColor);

        if (!outOfBoundsDetected)
        {
            g2.fillOval(xBall - rBall, yBall - rBall, 2 * rBall, 2 * rBall);
        }

        // Target
        int rTarget = (int)(target.getRadius() * gridAvg);
        int xTarget = (int)(target.getPosX() * xGridInterval + xCenter);
        int yTarget = (int)(yCenter - target.getPosY() * yGridInterval);

        g2.setColor(targetFillColor);
        g2.fillOval(xTarget - rTarget, yTarget - rTarget, 2 * rTarget, 2 * rTarget);

        g2.setColor(targetEdgeColor);
        g2.setStroke(new BasicStroke(targetEdgeSize));
        g2.drawOval(xTarget - rTarget, yTarget - rTarget, 2 * rTarget, 2 * rTarget);

        // Score
        g2.setFont(scoreFont);
        g2.drawString("Punkty: " + (int)points, 1010, 630);
        g2.drawString("Suma punktów: " + (int)totalPoints, 1010, 660);

        // Mode
        if (gameMode == GameMode.SANDBOX)
        {
            g2.drawString("Tryb Swobodny", 1010, 720);
        }
        else
        {
            g2.drawString("Tryb Pozimów", 1010, 720);
            g2.drawString("Poziom #" + levels[levelNumber].ID, 1010, 750);
        }

        // Debug purposes
        if(DEBUG_MODE)
        {
            g2.setColor(debugColor);
            g2.setFont(debugFont);
            g2.drawString("FPS: " + framesPerSecond, 0, 10);
            g2.drawLine(xBall, yBall, xBall + (int) (ball.getVelocityVector().x * gridAvg * 0.1), yBall - (int) (ball.getVelocityVector().y * gridAvg * 0.1));

            int rBounce = (int) (ball.getRadius() * gridAvg);
            int xBounce = (int) (lastBouncePos.x * xGridInterval + xCenter);
            int yBounce = (int) (yCenter - lastBouncePos.y * yGridInterval);

            g2.fillOval(xBounce - rBounce, yBounce - rBounce, 2 * rBounce, 2 * rBounce);
            g2.drawLine(xBounce, yBounce, xBounce + (int) (50. * lastBounceN.x), yBounce - (int) (50. * lastBounceN.y));
        }

        // Start-stop icon
        if (currentGameState == GameState.WAITING)
        {
            g2.drawImage(StartIcon, 925, 25, 50, 50, null);
        }
        else
        {
            g2.drawImage(StopIcon, 925, 25, 50, 50, null);
        }

        // Menu
        if (currentGameState == GameState.MENU)
        {
            g2.setColor(menuColor);
            g2.fillRect(250, 250, 500, 500);

            g2.setColor(functionColor);
            g2.fillRect(365, 395, 270, 50);

            g2.setColor(debugColor);
            g2.fillRect(365, 485, 270, 50);

            g2.setColor(mainGridColor);
            g2.setFont(menuFont);
            g2.drawString("MENU", 440, 350);
            g2.drawString("Swobodny", 400, 432);
            g2.drawString("Poziomy", 420, 522);
        }

        g.dispose();
    }
}