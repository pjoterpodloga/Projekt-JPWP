package src;

import src.Utilities.Vector3D;

public class Target
{
    private double radius;
    private Vector3D position;

    // Constructors
    public Target()
    {
        radius = 0.5;
        position = new Vector3D(0);
    }
    public Target(double r, Vector3D p)
    {
        radius = r;
        position = new Vector3D(p);
    }

    // Getters //
    public double getPosX()
    {
        return position.x;
    }
    public double getPosY()
    {
        return position.y;
    }
    public double getRadius()
    {
        return radius;
    }

    // Setters //
    public void setPosX(double x)
    {
        position.x = x;
    }
    public void setPosY(double y)
    {
        position.y = y;
    }
    public void setRadius(double r)
    {
        radius = r;
    }
}
