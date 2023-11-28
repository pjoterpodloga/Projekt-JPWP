package src;

public class Poly1Equ extends Equation{

    public Poly1Equ() {
        this.length = 100;
        this.optimalSize = this. length;

        this.x = new double[this.length];
        this.y = new double[this.length];

        this.A = 1.; // A coefficient
        this.B = -1.; // Y Offset

        this.epsilon = 100;

        this.xBoxGrid = 1;
        this.yBoxGrid = 1;

        this.type = EquType.POLY1;
    }

    @Override
    public void calculateValues() {

        if (!update) { return;}

        update = false;

        x = Utils.linspace(this.start, this.stop, length);
        for (int i = 0; i < this.length; i += 1)
        {
            y[i] = this.A * x[i] + this.B;
        }
    }

    @Override
    public void optimizeSize()
    {
        double interval = this.stop - this.start;
        double gridFactor = Math.abs(this.A * this.B);

        if (gridFactor < 1.)
        {
            gridFactor = 1.;
        }

        if (gridFactor > 100.)
        {
            gridFactor = 100.;
        }

        int optimalSize = (int)(400 * gridFactor * interval);
        if (Math.abs(optimalSize - this.optimalSize) > epsilon)
        {
            this.optimalSize = optimalSize;
            resize(optimalSize);
        }
    }
}
