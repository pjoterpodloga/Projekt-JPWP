package src;

public class Ball {
    private int radius;
    private double mass;
    private double xPos, yPos;
    private double xVelocity, yVelocity;
    private double xAcceleration, yAcceleration;
    private double xForce, yForce;

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

        xForce = 0;
        yForce = 0;
    }

    public void setRadius(int r)
    {
        radius = r;
    }
    public void setxPos(int x)
    {
        xPos = x;
    }
    public void setyPos(int y)
    {
        yPos = y;
    }
    public void setxAcceleration(double a)
    {
        xAcceleration = a;
    }
    public void setyAcceleration(double a)
    {
        yAcceleration = a;
    }
    public void calculateDisplacement(double dt)
    {
        xAcceleration += xForce / mass;
        yAcceleration += yForce / mass;

        double xDis = xVelocity * dt + xAcceleration * dt * dt / 2.;
        double yDis = yVelocity * dt + yAcceleration * dt * dt / 2.;

        xVelocity += xAcceleration * dt;
        yVelocity += yAcceleration * dt;

        xPos += xDis;
        yPos += yDis;
    }
    public int getRadius()
    {
        return radius;
    }
    public int getxPos()
    {
        return (int)xPos;
    }
    public int getyPos()
    {
        return (int)yPos;
    }
}
