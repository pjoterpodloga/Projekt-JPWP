package src;

public class Vector3D {
    public double x, y, z;
    public Vector3D()
    {
        x = 0;
        y = 0;
        z = 0;
    }
    public Vector3D(double xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }
    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void scale(double s)
    {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }
}
