package src;

public class ExpEqu extends Equation {

    public ExpEqu() {
        this.length = 100;
        this.optimalSize = this. length;

        this.x = new double[this.length];
        this.y = new double[this.length];

        this.A =  1.; // Scale
        this.B = -1.; // Rise rate
        this.C =  1.; // X Offset
        this.D =  0.; // Y Offset

        this.epsilon = 100;

        this.xBoxGrid = 1;
        this.yBoxGrid = 1;

        this.type = EquType.EXP;
    }

    @Override
    public void calculateValues() {

        if (!update) { return;}

        update = false;

        x = Utils.linspace(this.start, this.stop, length);
        for (int i = 0; i < this.length; i += 1)
        {
            y[i] = this.A*Math.exp(this.B*x[i] + this.C) + this.D;
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
