package src;

public abstract class Equation {
    public static Equation createEquation(EquType type) {
        Equation returnEqu = switch (type) {
            case POLY1 -> null;
            case POLY2 -> null;
            case POLY3 -> null;
            case POLY4 -> null;
            case POLY5 -> null;
            case POLY6 -> null;
            case SIN -> new SinEqu();
            case TAN -> null;
            case EXP -> null;
            case LOG -> null;
            default -> null;
        };

        return returnEqu;
    }

    protected Equation()
    {

    }

    public void calculateValues()
    {

    }

    final public void resize(int l)
    {
        this.length = l;
        this.x = new double[l];
        this.y = new double[l];

        calculateValues();
    }

    final public void setInterval(double x1, double x2) {
        this.start = x1;
        this.stop = x2;
    }

    final public EquType getType() {
        return type;
    }

    final public double getX(int n) {
        return x[n];
    }

    final public double getY(int n) {
        return y[n];
    }

    final double getA()
    {
        return A;
    }
    final double getB()
    {
        return B;
    }
    final double getC()
    {
        return C;
    }
    final double getD()
    {
        return D;
    }

    final double getE()
    {
        return E;
    }

    final double getF()
    {
        return F;
    }

    final double getG()
    {
        return G;
    }


    final public int getLength()
    {
        return this.length;
    }

    //// Setters ////

    public void setA(double A)
    {
        this.A = A;
        calculateValues();
    }
    public void setB(double B)
    {
        this.B = B;
        calculateValues();
    }
    public void setC(double C)
    {
        this.C = C;
        calculateValues();
    }
    public void setD(double D)
    {
        this.D = D;
        calculateValues();
    }
    public void setE(double E)
    {
        this.E = E;
        calculateValues();
    }
    public void setF(double F)
    {
        this.F = F;
        calculateValues();
    }
    public void setG(double G)
    {
        this.G = G;
        calculateValues();
    }

    protected EquType type;
    protected double[] x;
    protected double[] y;
    protected double A, B, C, D, E, F, G, start, stop;
    protected int length;
}
