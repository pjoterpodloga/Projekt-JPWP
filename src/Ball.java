package src;

public class Ball {
    private int radius;
    private double mass;
    private double xPos, yPos;
    private Vector3D velocity;
    private Vector3D acceleration;
    private boolean bounced;

    public Ball()
    {
        radius = 10;

        mass = 1;

        xPos = 0;
        yPos = 0;

        velocity = new Vector3D(0);

        acceleration = new Vector3D(0);

        bounced = false;
    }
    public Ball(int r)
    {
        radius = r;

        mass = 1;

        xPos = 0;
        yPos = 0;

        velocity = new Vector3D(0);

        acceleration = new Vector3D(0);

        bounced = false;
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
    public void setxVelocity(double xv) { velocity.x = xv; }
    public void setyVelocity(double yv) { velocity.y = yv; }
    public void setVelocity(Vector3D v) { velocity = v; }
    public void setxAcceleration(double xa)
    {
        acceleration.x = xa;
    }
    public void setyAcceleration(double ya)
    {
        acceleration.y = ya;
    }
    public void setBounced()  { this.bounced = true; }
    public void resetBounced() { this.bounced = false; }
    public boolean isBounced() { return bounced; }
    public void calculateDisplacement(double dt)
    {
        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;

        double xDis = velocity.x * dt;
        double yDis = velocity.y * dt;

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
    public double getxVelocity() { return velocity.x; }
    public double getyVelocity() { return velocity.y; }
    public double getVelocityModulus() { return Utilis.norm(velocity);}
    public double getxAcceleration() { return acceleration.x; }
    public double getyAcceleration() { return acceleration.y; }
}
