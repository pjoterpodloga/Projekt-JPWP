package src;

public class SinEqu extends Equation {

    public SinEqu() {
        this.length = 100;
        this.x = new double[this.length];
        this.y = new double[this.length];

        this.A = 2.; // Amplitude
        this.B = 1.; // Frequency
        this.C = 0.; // Phase
        this.D = 0.; // Offset
    }

    @Override
    public void calculateValues() {
        x = Utilis.linspace(this.start, this.stop, length);
        for (int i = 0; i < this.length; i += 1)
        {
            y[i] = this.A * Math.sin(2. * Math.PI * ( x[i] * this.B - this.C)) + this.D;
        }
    }
}

