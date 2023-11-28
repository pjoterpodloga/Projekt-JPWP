package src;

public abstract class Equation {
    public static Equation createEquation(EquType type) {

        Equation returnEqu = switch (type) {
            case POLY1 -> new Poly1Equ();
            case POLY2 -> new Poly2Equ();
            case POLY3 -> new Poly3Equ();
            case POLY4 -> new Poly4Equ();
            case POLY5 -> null;
            case POLY6 -> null;
            case SIN -> new SinEqu();
            case TAN -> null;
            case EXP -> new ExpEqu();
            case LOG -> null;
            default -> null;
        };

        return returnEqu;
    }

    protected Equation() { }

    public void calculateValues() { }

    final public void resize(int l)
    {
        this.length = l;
        this.x = new double[l];
        this.y = new double[l];
        update = true;
    }

    final public void setInterval(double x1, double x2) {
        this.start = x1;
        this.stop = x2;

        update = true;
    }
    public void optimizeSize() { }

    protected EquType type;
    protected double[] x;
    protected double[] y;
    protected double A, B, C, D, E, F, G, start, stop;
    protected int length, optimalSize, epsilon;
    protected int xBoxGrid, yBoxGrid;
    protected boolean update = true;

    //// Getters ////
    final public EquType getType() {
        return type;
    }
    final public double getX(int n)
    {
        return x[n];
    }
    final public double getY(int n)
    {
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
        update = true;
    }
    public void setB(double B)
    {
        this.B = B;
        update = true;
    }
    public void setC(double C)
    {
        this.C = C;
        update = true;
    }
    public void setD(double D)
    {
        this.D = D;
        update = true;
    }
    public void setE(double E)
    {
        this.E = E;
        update = true;
    }
    public void setF(double F)
    {
        this.F = F;
        update = true;
    }
    public void setG(double G)
    {
        this.G = G;
        update = true;
    }
}
