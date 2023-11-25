package src;

import java.util.concurrent.Semaphore;

public class Timer extends Thread{

    //// Private fields ////
    private long tickCounter = 0;
    private long lastTicks = 0;
    private boolean running = false;

    //// Public fields ////
    public final long NS_PER_TICK = 1_00_000;
    public Semaphore mutex = new Semaphore(1);
    //// Private methods ////

    //// Public methods ////

    public long getElapsedTicks()
    {
        try { mutex.acquire(); }
        catch (Exception e) { e.printStackTrace(); }

        long elapsedTicks = tickCounter - lastTicks;
        lastTicks = tickCounter;

        mutex.release();

        return elapsedTicks;
    }

    public double getElapsedTime()
    {
        return (double)(getElapsedTicks() * NS_PER_TICK);
    }

    public double getElapsedTime_ms()
    {
        return (getElapsedTime() / 1_000_000.);
    }
    public boolean isRunning()
    {
        try { mutex.acquire(); }
        catch (Exception e) { e.printStackTrace(); }

        boolean returnValue = running;

        mutex.release();

        return returnValue;
    }

    public void close()
    {
        try { mutex.acquire(); }
        catch (Exception e) { e.printStackTrace(); }

        running = false;

        mutex.release();
    }

    public void run()
    {
        running = true;

        long currentTime = System.nanoTime();
        long lastTime;
        double deltaTime;
        double deltaTimeFix = 0;

        while (true)
        {
            lastTime = currentTime; // Save last got time
            currentTime = System.nanoTime(); // Get current time

            deltaTimeFix = ((double) (currentTime - lastTime) / NS_PER_TICK) + deltaTimeFix; // Calculate delta of time
            deltaTime = Math.floor(deltaTimeFix);
            deltaTimeFix = deltaTimeFix - deltaTime;

            try { Thread.sleep(1); mutex.acquire(); }
            catch (Exception e) { e.printStackTrace(); }

            tickCounter += deltaTime;

            if (!running)
            {
                break;
            }

            mutex.release();
        }

    }

}
