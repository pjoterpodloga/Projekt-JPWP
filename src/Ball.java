package src;

import src.Utilities.Vector3D;

import java.util.Vector;

public class Ball {

    //// Private fields ////
    private double radius;
    private Vector3D position;
    private Vector3D resetPos;
    private Vector3D velocity;
    private Vector3D acceleration;
    private int hitCount;
    private double hitCountdown;
    private boolean stuck;

    public Ball()
    {
        radius = 10;
        position = new Vector3D(0);
        velocity = new Vector3D(0);
        acceleration = new Vector3D(0);

        hitCountdown = 0;
        hitCount = 0;
        stuck = false;
    }
    public Ball(double r)
    {
        radius = r;
        position = new Vector3D(0);
        velocity = new Vector3D(0);
        acceleration = new Vector3D(0);

        hitCountdown = 0;
        hitCount = 0;
        stuck = false;
    }
    public void calculateDisplacement(double dt)
    {
        hitCountdown -= dt;

        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;

        Vector3D displacement = new Vector3D(velocity.x * dt, velocity.y * dt, 0);

        if (stuck)
        {
            return;
        }

        position.x += displacement.x;
        position.y += displacement.y;
    }
    public void calculateBounce(Vector3D n, Vector3D d)
    {
        if (Vector3D.norm(velocity) <= 2)
        {
            stuck = true;
            return;
        }

        if (hitCountdown > 0)
        {
            return;
        }

        Vector3D vn = new Vector3D(n);
        Vector3D.scale(vn, 2*Vector3D.dot(n, d));
        Vector3D b = Vector3D.normalized(Vector3D.sub(d, vn));
        velocity = Vector3D.scale(b, Vector3D.norm(velocity) * 0.8);

        hit();
    }
    public void hit()
    {
        hitCount++;
        hitCountdown = 0.2;
    }

    public void reset()
    {
        position.x = resetPos.x;
        position.y = resetPos.y;

        velocity.x = 0;
        velocity.y = 0;

        hitCount = 0;
        hitCountdown = 0;

        stuck = false;
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
    public void setResetPos(Vector3D resPos)
    {
        resetPos = resPos;
    }
    public void setAcceleration(Vector3D a)
    {
        acceleration = a;
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
    public double getHitCountdown()
    {
        return hitCountdown;
    }
    public Vector3D getVelocityVector()
    {
        return velocity;
    }
    public Vector3D getAcceleration()
    {
        return acceleration;
    }
    public int getHitCount()
    {
        return hitCount;
    }
}
