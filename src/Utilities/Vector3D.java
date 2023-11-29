package src.Utilities;

public class Vector3D {
    public double x, y, z;
    public Vector3D()
    {
        x = 1 / Math.sqrt(3);
        y = 1 / Math.sqrt(3);
        z = 1 / Math.sqrt(3);
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

    static public double dot(Vector3D v1, Vector3D v2)
    {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }
    static public double norm(Vector3D v)
    {
        return Math.sqrt(dot(v, v));
    }
    static public Vector3D cross(Vector3D v1, Vector3D v2)
    {
        double x = v1.y * v2.z - v1.z * v2.y;
        double y = v1.z * v2.x - v1.x * v2.z;
        double z = v1.x * v2.y - v1.y * v2.x;

        return new Vector3D(x, y, z);
    }
    static public Vector3D normalized(Vector3D v)
    {
        double d = norm(v);
        return new Vector3D(v.x/d, v.y/d, v.z/d);
    }
    static public Vector3D add(Vector3D v1, Vector3D v2)
    {
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    static public Vector3D sub(Vector3D v1, Vector3D v2)
    {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    static public Vector3D scale(Vector3D v, double s)
    {
        v.x *= s;
        v.y *= s;
        v.z *= s;

        return v;
    }
}
