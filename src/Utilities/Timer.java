package src.Utilities;

public class Timer {

    // Private fields //
    private long tickCounter = 0;
    private long elapsedTicks;
    private long elapsedTime_ns;
    private final long ticks;
    private final long nsPerTicks;
    private long lastTime = 0;
    private long totalElapsedTime = 0;

    // Public methods //
    public Timer()
    {
        ticks = 64;
        this.nsPerTicks = 1_000_000_000L / ticks;
    }
    // Ticks per second
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
    // Delta time in ns
    public long getElapsedTime_ns()
    {
        long currentTime = System.nanoTime();
        elapsedTime_ns = (currentTime - lastTime);
        lastTime = currentTime;

        totalElapsedTime += elapsedTime_ns;

        return elapsedTime_ns;
    }
    // Delta time in us
    public double getElapsedTime_us()
    {
        getElapsedTicks();
        return (double)elapsedTime_ns / 1_000.;
    }
    // Delta time in ms
    public double getElapsedTime_ms()
    {
        getElapsedTicks();
        return (double)elapsedTime_ns / 1_000_000.;
    }
    // Total elapsed time in ns
    public double getTotalElapsedTime_ns()
    {
        getElapsedTicks();
        return (double)totalElapsedTime;
    }
    // Total elapsed time in us
    public double getTotalElapsedTime_us()
    {
        getElapsedTicks();
        return (double)totalElapsedTime / 1_000.;
    }
    // Total elapsed time in ms
    public double getTotalElapsedTime_ms()
    {
        getElapsedTicks();
        return (double)totalElapsedTime / 1_000_000.;
    }
    // Total ticks
    public int getTicks()
    {
        return (int) ticks;
    }

}
