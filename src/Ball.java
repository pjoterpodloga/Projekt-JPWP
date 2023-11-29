package src;

import src.Utilities.Vector3D;

public class Ball {

    //// Private fields ////
    private double radius;
    private Vector3D position;
    public PhysicsProperties physicsProperties;
    private int hitCount;
    private int hitCountdown;
    private boolean stuck;

    public Ball()
    {
        radius = 10;
        position = new Vector3D(0);
        physicsProperties = new PhysicsProperties(1);

        hitCountdown = 0;
        hitCount = 0;
        stuck = false;
    }
    public Ball(double r)
    {
        radius = r;
        position = new Vector3D(0);
        physicsProperties = new PhysicsProperties(1);

        hitCountdown = 0;
        hitCount = 0;
        stuck = false;
    }
    public void calculateDisplacement(double dt)
    {
        hitCountdown--;

        physicsProperties.updateVelocity(dt);

        Vector3D velocity = physicsProperties.getVelocity();

        Vector3D displacement = new Vector3D(velocity.x * dt, velocity.y * dt, 0);

        position.x += displacement.x;
        position.y += displacement.y;
    }
    public void hit()
    {
        hitCount++;
        hitCountdown = 3;
    }
    //// Setters ////
    public void setRadius(double r)
    {
        radius = r;
    }
    public void setxPos(double x)
    {
       position.x = x;
    }
    public void setyPos(double y)
    {
        position.y = y;
    }

    //// Getters ////
    public double getRadius()
    {
        return radius;
    }
    public double getxPos()
    {
        return position.x;
    }
    public double getyPos()
    {
        return position.y;
    }
    public int getHitCountdown()
    {
        return hitCountdown;
    }
    public Vector3D getVelocityVector()
    {
        return physicsProperties.getVelocity();
    }
    public Vector3D getAccelerationVector()
    {
        return physicsProperties.getAcceleration();
    }
    public int getHitCount()
    {
        return hitCount;
    }
}
