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
        Vector3D vn = new Vector3D(n);

        normalForce = Vector3D.scale(vn, -Vector3D.dot(n, gravityForce));
        appliedForce = Vector3D.add(gravityForce, normalForce);
        //frictionForce = Vector3D.scale(Vector3D.normalized(appliedForce), -material.getuCoefficient() * Vector3D.norm(normalForce));

        frictionForce = new Vector3D(0);

        if (Vector3D.norm(frictionForce) > Vector3D.norm(appliedForce))
        {
            frictionForce = new Vector3D(appliedForce);
        }
    }
    private void calculateForceFly(Vector3D a)
    {
        normalForce = new Vector3D(0);
        appliedForce = new Vector3D(0);
        frictionForce = new Vector3D(velocity);
        Vector3D.scale(frictionForce, -0.1);
    }
    private Vector3D calculateBounce(Vector3D n, Vector3D d)
    {
        Vector3D vn = new Vector3D(n);
        Vector3D.scale(vn, 2*Vector3D.dot(n, d));
        Vector3D b = Vector3D.normalized(Vector3D.sub(d, vn));
        return Vector3D.scale(b, Vector3D.norm(velocity) * 0.2);
    }
    public void calculateAcceleration(Vector3D a, Vector3D n, Vector3D d)
    {
        Vector3D newVelocity;

        gravityForce = a;

        if (n == null)
        {
            calculateForceFly(a);
            //System.out.println("Fly");
        }
        // TODO: Improve roll calculation
        else
        {
            newVelocity = calculateBounce(n, d);

            if(Vector3D.norm(newVelocity) <= 0.5)
            {
                calculateForceRoll(n);

                System.out.println("Roll");
            }

            //velocity = newVelocity;
            System.out.println("Bounce");
        }

        incidentForce.x = gravityForce.x + normalForce.x + appliedForce.x + frictionForce.x;
        incidentForce.y = Math.round((gravityForce.y + normalForce.y + appliedForce.y + frictionForce.y) * 1e9) / 1e9;

        acceleration = incidentForce;

        System.out.println("Ball acceleration: x=" + acceleration.x + " | y=" + acceleration.y);
        System.out.println("Ball velocity: x=" + velocity.x + " | y=" + velocity.y);
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
