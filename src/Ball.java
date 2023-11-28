package src;

public class Ball {

    //// Private fields ////
    private double radius;
    private double mass;
    private Vector3D pos;
    private Vector3D velocity;
    private Vector3D acceleration;
    private int hitCount;
    private int hitCountdown;
    private boolean stuck;

    public Ball()
    {
        radius = 10;
        mass = 1;
        pos = new Vector3D(0);
        velocity = new Vector3D(0);
        acceleration = new Vector3D(0);

        lastDisplacement = new double[10];
        hitCountdown = 0;
        hitCount = 0;
        stuck = false;


        for (int i = 0; i < lastDisplacement.length; i += 1)
        {
            lastDisplacement[i] = 0;
        }
    }
    public Ball(double r)
    {
        radius = r;
        mass = 1;
        pos = new Vector3D(0);
        velocity = new Vector3D(0);
        acceleration = new Vector3D(0);

        lastDisplacement = new double[10];
        hitCountdown = 0;
        hitCount = 0;
        stuck = false;

        for (int i = 0; i < lastDisplacement.length; i += 1)
        {
            lastDisplacement[i] = 0;
        }
    }

    double[] lastDisplacement;
    double avgDisplacement;

    private void calculateAvgDisplacement(double lastDisplacement)
    {
        avgDisplacement = 0;

        for (int i = 1; i < this.lastDisplacement.length; i += 1)
        {
            this.lastDisplacement[i] = this.lastDisplacement[i - 1];
            avgDisplacement += this.lastDisplacement[i];
        }

        this.lastDisplacement[0] = lastDisplacement;
        avgDisplacement /= this.lastDisplacement.length;
    }
    public void calculateDisplacement(double dt)
    {
        hitCountdown--;

        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;

        Vector3D displacement = new Vector3D(velocity.x * dt, velocity.y * dt, 0);

        calculateAvgDisplacement(Utils.norm(displacement));

        if (avgDisplacement >= 1e-3)
        {
            stuck = false;
        }

        if (!(stuck))
        {
            pos.x += displacement.x;
            pos.y += displacement.y;
        }
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
       pos.x = x;
    }
    public void setyPos(double y)
    {
        pos.y = y;
    }
    public void setxVelocity(double xv)
    {
        velocity.x = xv;
    }
    public void setyVelocity(double yv)
    {
        velocity.y = yv;
    }
    public void setVelocity(Vector3D v) { velocity = v; }
    public void setxAcceleration(double xa)
    {
        acceleration.x = xa;
    }
    public void setyAcceleration(double ya)
    {
        acceleration.y = ya;
    }
    public void resetHitCount()
    {
        hitCount = 0;
    }
    public void setStuck()
    {
        stuck = true;
    }
    //// Getters ////
    public double getRadius()
    {
        return radius;
    }
    public double getxPos()
    {
        return pos.x;
    }
    public double getyPos()
    {
        return pos.y;
    }
    public double getxVelocity()
    {
        return velocity.x;
    }
    public double getyVelocity()
    {
        return velocity.y;
    }
    public Vector3D getVelocityVector()
    {
        return velocity;
    }
    public double getVelocityModulus()
    {
        return Utils.norm(velocity);
    }
    public double getxAcceleration()
    {
        return acceleration.x;
    }
    public double getyAcceleration()
    {
        return acceleration.y;
    }
    public int getHitCount()
    {
        return hitCount;
    }
    public int getHitCountdown()
    {
        return hitCountdown;
    }
    public boolean isStuck()
    {
        return stuck;
    }
}
