package src.Utilities;

public class Graph {

    // Private fields //
    private double dx;  // Relative x position of graph
    private double dy;  // Relative y position of graph
    private double xGridInterval;   // X sub grid interval
    private double yGridInterval;   // Y sub grid interval

    public Graph(double dx, double dy, double griddx, double griddy)
    {
        this.dx = dx;
        this.dy = dy;
        this.xGridInterval = griddx;
        this.yGridInterval = griddy;
    }

    // Setters //
    public void setdx(double dx)
    {
        this.dx = dx;
    }
    public void setdy(double dy)
    {
        this.dy = dy;
    }
    public void setxGridInterval(double xGridInt)
    {
        xGridInterval = xGridInt;
    }
    public void setyGridInterval(double yGridInt)
    {
        yGridInterval = yGridInt;
    }

    // Getters //
    public double getdx()
    {
        return dx;
    }
    public double getdy()
    {
        return dy;
    }
    public double getxGridInterval()
    {
        return xGridInterval;
    }
    public double getyGridInterval()
    {
        return yGridInterval;
    }
}
