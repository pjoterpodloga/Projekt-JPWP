package src;

import java.util.Vector;

public class Utilis {
    static double[] linspace(double start, double stop, int n)
    {
        double[] values = new double[n];

        values[0] = start;
        values[n - 1] = stop;

        double increment = (stop - start) / (n - 1.);

        for (int i = 1; i < n - 1; i += 1)
        {
            values[i] = values[i - 1] + increment;
        }

        return values;
    }
    static double dot(Vector3D v1, Vector3D v2)
    {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }
    static double norm(Vector3D v)
    {
        return Math.sqrt(dot(v, v));
    }
    static Vector3D cross(Vector3D v1, Vector3D v2)
    {
        double x = v1.y * v2.z - v1.z * v2.y;
        double y = v1.z * v2.x - v1.x * v2.z;
        double z = v1.x * v2.y - v1.y * v2.x;

        return new Vector3D(x, y, z);
    }
    static Vector3D normalized(Vector3D v)
    {
        double d = norm(v);
        return new Vector3D(v.x/d, v.y/d, v.z/d);
    }
    static Vector3D addVector3D(Vector3D v1, Vector3D v2)
    {
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    static Vector3D subVector3D(Vector3D v1, Vector3D v2)
    {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
}
