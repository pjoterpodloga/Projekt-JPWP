package src;

public class Timer {

    //// Private fields ////
    private long tickCounter = 0;
    private long elapsedTicks;
    private long elapsedTime_ns;
    private long ticks;
    private long nsPerTicks;
    private long lastTime = 0;
    private long totalElapsedTime = 0;

    //// Private methods ////

    //// Public methods ////
    public Timer()
    {
        ticks = 64;
        this.nsPerTicks = 1_000_000_000L / ticks;
    }
    public Timer(long ticks)
    {
        if (ticks > 128)
        {
            ticks = 128;
        }

        this.ticks = ticks;
        this.nsPerTicks = 1_000_000_000L / this.ticks;
    }
    public long getElapsedTicks()
    {
        elapsedTicks =  getElapsedTime_ns() / nsPerTicks;
        tickCounter += elapsedTicks;

        return elapsedTicks;
    }
    public long getElapsedTime_ns()
    {
        long currentTime = System.nanoTime();
        elapsedTime_ns = (currentTime - lastTime);
        lastTime = currentTime;

        totalElapsedTime += elapsedTime_ns;

        return elapsedTime_ns;
    }
    public double getElapsedTime_us()
    {
        getElapsedTicks();
        return (double)elapsedTime_ns / 1_000.;
    }
    public double getElapsedTime_ms()
    {
        getElapsedTicks();
        return (double)elapsedTime_ns / 1_000_000.;
    }
    public double getTotalElapsedTime_ns()
    {
        getElapsedTicks();
        return (double)totalElapsedTime;
    }
    public double getTotalElapsedTime_us()
    {
        getElapsedTicks();
        return (double)totalElapsedTime / 1_000.;
    }
    public double getTotalElapsedTime_ms()
    {
        getElapsedTicks();
        return (double)totalElapsedTime / 1_000_000.;
    }
    public int getTicks()
    {
        return (int) ticks;
    }

}
