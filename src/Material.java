package src;

public class Material {

    private double kCoefficient;    // Elasticity coefficient
    private double uCoefficient;    // Friction coefficient

    public Material(double k, double u)
    {
        this.kCoefficient = k;
        this.uCoefficient = u;
    }

    public double getkCoefficient()
    {
        return kCoefficient;
    }
    public double getuCoefficient()
    {
        return uCoefficient;
    }

}
