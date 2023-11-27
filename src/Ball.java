package src;

public class Ball {
    private int radius;
    private double mass;
    private double xPos, yPos;
    private double xVelocity, yVelocity;
    private double xAcceleration, yAcceleration;

    public Ball()
    {
        radius = 10;

        mass = 1;

        xPos = 0;
        yPos = 0;

        xVelocity = 0;
        yVelocity = 0;

        xAcceleration = 0;
        yAcceleration = 0;
    }
    public Ball(int r)
    {
        radius = r;

        mass = 1;

        xPos = 0;
        yPos = 0;

        xVelocity = 0;
        yVelocity = 0;

        xAcceleration = 0;
        yAcceleration = 0;
    }

    public void setRadius(int r)
    {
        radius = r;
    }
    public void setxPos(double x)
    {
        xPos = x;
    }
    public void setyPos(double y)
    {
        yPos = y;
    }
    public void setxVelocity(double xv) { xVelocity = xv; }
    public void setyVelocity(double yv) { yVelocity = yv; }
    public void setxAcceleration(double xa)
    {
        xAcceleration = xa;
    }
    public void setyAcceleration(double ya)
    {
        yAcceleration = ya;
    }
    public void calculateDisplacement(double dt)
    {
        xVelocity += xAcceleration * dt;
        yVelocity += yAcceleration * dt;

        double xDis = xVelocity * dt;
        double yDis = yVelocity * dt;

        xPos += xDis;
        yPos += yDis;
    }
    public int getRadius()
    {
        return radius;
    }
    public double getxPos()
    {
        return xPos;
    }
    public double getyPos()
    {
        return yPos;
    }
    public double getxVelocity() { return xVelocity; }
    public double getyVelocity() { return yVelocity; }
    public double getVelocityModulus() { return Math.sqrt(xVelocity*xVelocity + yVelocity*yVelocity);}
    public double getxAcceleration() { return xAcceleration; }
    public double getyAcceleration() { return yAcceleration; }
}
