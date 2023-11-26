package src;

public class SinEqu extends Equation {

    public SinEqu() {
        this.length = 100;
        this.optimalSize = this. length;

        this.x = new double[this.length];
        this.y = new double[this.length];

        this.A = 1.; // Amplitude
        this.B = 1.; // Frequency
        this.C = 0.; // Phase
        this.D = 0.; // Offset

        this.epsilon = 100;

        this.xBoxGrid = 1;
        this.yBoxGrid = 1;

        this.type = EquType.SIN;
    }

    @Override
    public void calculateValues() {

        if (!update) { return;}

        update = false;

        x = Utilis.linspace(this.start, this.stop, length);
        for (int i = 0; i < this.length; i += 1)
        {
            y[i] = this.A * Math.sin(2. * Math.PI * ( x[i] * this.B - this.C)) + this.D;
        }
    }

    @Override
    public void optimazeSize()
    {
        double interval = this.stop - this.start;
        double gridFactor = Math.abs(this.A * this.B);

        if (gridFactor > 100.)
        {
            gridFactor = 100.;
        }

        int optimalSize = (int)(200 * gridFactor * interval);
        if (Math.abs(optimalSize - this.optimalSize) > epsilon)
        {
            this.optimalSize = optimalSize;
            resize(optimalSize);
        }
    }
}

