package src;

import java.awt.*;

public class Graph {

    Graph(Point c, int x, int y)
    {
        this.center = c;
        this.xGridInterval = x;
        this.yGridInterval = y;
    }

    private Point center;
    private int xGridInterval;
    private int yGridInterval;

    //// Setters ////
    public void setCenter(int x, int y)
    {
        center = new Point(x, y);
    }
    public void setCenter(Point c)
    {
        center = c;
    }
    public void setxGridInterval(int xGridInt)
    {
        xGridInterval = xGridInt;
    }
    public void setyGridInterval(int yGridInt)
    {
        yGridInterval = yGridInt;
    }

    //// Getters ////
    public Point getCenter()
    {
        return center;
    }
    public int getxCenter()
    {
        return center.x;
    }
    public int getyCenter()
    {
        return center.y;
    }
    public int getxGridInterval()
    {
        return xGridInterval;
    }
    public int getyGridInterval()
    {
        return yGridInterval;
    }
}
