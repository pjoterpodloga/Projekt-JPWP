package src;

import src.Utilities.Vector3D;

import java.util.Vector;

public class PhysicsProperties {
    private Vector3D frictionForce;
    private Vector3D normalForce;
    private Vector3D gravityForce;
    private Vector3D appliedForce;
    private Vector3D incidentForce;

    private Vector3D velocity;
    private Vector3D acceleration;
    private double mass;

    public Material material;

    public PhysicsProperties(double mass)
    {
        frictionForce = new Vector3D(0);
        normalForce = new Vector3D(0);
        gravityForce = new Vector3D(0);
        appliedForce = new Vector3D(0);

        incidentForce = new Vector3D(0);

        velocity = new Vector3D(0);
        acceleration = new Vector3D(0);

        material = new Material(0.75, 0.5);

        this.mass = mass;
    }

   private void calculateForceRoll(Vector3D n)
    {
        normalForce = Vector3D.scale(n, Vector3D.dot(n, gravityForce));
        appliedForce = Vector3D.sub(gravityForce, normalForce);
        normalForce = Vector3D.scale(normalForce, -1);
        frictionForce = Vector3D.scale(normalForce, material.getuCoefficient());
    }
    private void calculateForceFly(Vector3D a)
    {

    }
    private void calculateBounce(Vector3D n, Vector3D d)
    {
        Vector3D vn = Vector3D.scale(n, 2.* Vector3D.dot(d, n));
        Vector3D b = Vector3D.normalized(Vector3D.sub(d, vn));
        velocity = Vector3D.scale(b, Vector3D.norm(velocity) * 0.7);
    }
    public void calculateAcceleration(Vector3D a, Vector3D n, Vector3D d)
    {
        gravityForce = a;

        if (n == null && d == null)
        {
            calculateForceFly(a);
        }
        // TODO: Improve roll calculation
        else if (Math.abs(Vector3D.dot(Vector3D.normalized(a), d)) >= 0.999)
        {
            calculateForceRoll(n);
        }
        else
        {
            calculateBounce(n, d);
        }

        incidentForce.x = gravityForce.x + normalForce.x + appliedForce.x + frictionForce.x;
        incidentForce.y = gravityForce.y + normalForce.y + appliedForce.y + frictionForce.y;

        acceleration = incidentForce;
    }
    public void updateVelocity(double dt)
    {
        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;
    }

    //// Getters ////
    public double getMass()
    {
        return mass;
    }
    public Vector3D getAcceleration() {
        return acceleration;
    }
    public Vector3D getVelocity() {
        return velocity;
    }
}
